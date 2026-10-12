package com.app.hatli.core.data.request

import com.app.hatli.core.data.request.mapper.RequestFirestoreMapper
import com.app.hatli.core.model.CancelledBy
import com.app.hatli.core.model.ChatMessage
import com.app.hatli.core.model.CreateRequestInput
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestRating
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.nextRunnerStatus
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRequestDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    private val requests get() = firestore.collection(COLLECTION)

    suspend fun create(
        requesterId: String,
        requesterName: String?,
        input: CreateRequestInput,
    ): String {
        val doc = requests.document()
        val data = RequestFirestoreMapper.toCreateRequestMap(
            input = input,
            requestId = doc.id,
            requesterId = requesterId,
            requesterName = requesterName ?: input.requesterName,
        )
        doc.set(data).await()
        return doc.id
    }

    suspend fun accept(
        requestId: String,
        runnerId: String,
        runnerName: String?,
        requesterNameFallback: String?,
    ) {
        val ref = requests.document(requestId)
        firestore.runTransaction { tx ->
            val snap = tx.get(ref)
            if (!snap.exists()) throw AppRequestException("Request not found.")
            val status = snap.getString(FIELD_STATUS)
            val existingRunner = snap.getString(FIELD_RUNNER_ID)
            if (status != RequestStatus.Pending.name || !existingRunner.isNullOrBlank()) {
                throw AppRequestException("Request is no longer available.")
            }
            val history = RequestFirestoreMapper.readHistory(snap).toMutableList().apply {
                add(RequestFirestoreMapper.buildHistoryEntry(RequestStatus.Accepted.name, runnerId))
            }
            val requesterName = snap.getString(FIELD_REQUESTER_NAME) ?: requesterNameFallback
            val updates = RequestFirestoreMapper.buildAcceptUpdateMap(runnerId, runnerName, requesterName, history)
            tx.update(ref, updates)
        }.await()
    }

    suspend fun advanceStatus(
        requestId: String,
        uid: String,
        toStatus: RequestStatus,
    ) {
        val ref = requests.document(requestId)
        firestore.runTransaction { tx ->
            val snap = tx.get(ref)
            if (!snap.exists()) throw AppRequestException("Request not found.")
            if (snap.getString(FIELD_RUNNER_ID) != uid) {
                throw AppRequestException("Only the assigned runner can update status.")
            }
            val current = RequestFirestoreMapper.toRequestStatus(snap.getString(FIELD_STATUS))
                ?: throw AppRequestException("Invalid status.")
            val category = RequestFirestoreMapper.toRequestCategory(snap.getString(FIELD_CATEGORY))
                ?: RequestCategory.Custom
            val stub = Request(
                id = requestId,
                requesterId = "",
                category = category,
                status = current,
                title = "",
                description = "",
                deliveryFee = 0.0,
            )
            val expected = stub.nextRunnerStatus()
                ?: throw AppRequestException("No further runner action for this status.")
            if (toStatus != expected) {
                throw AppRequestException("Invalid status transition.")
            }
            val history = RequestFirestoreMapper.readHistory(snap).toMutableList().apply {
                add(RequestFirestoreMapper.buildHistoryEntry(toStatus.name, uid))
            }
            val updates = RequestFirestoreMapper.buildAdvanceStatusUpdateMap(toStatus, history)
            tx.update(ref, updates)
        }.await()
    }

    suspend fun confirmDelivery(requestId: String, requesterId: String) {
        val ref = requests.document(requestId)
        firestore.runTransaction { tx ->
            val snap = tx.get(ref)
            if (!snap.exists()) throw AppRequestException("Request not found.")
            if (snap.getString(FIELD_REQUESTER_ID) != requesterId) {
                throw AppRequestException("Only the requester can confirm delivery.")
            }
            if (snap.getString(FIELD_STATUS) != RequestStatus.Delivered.name) {
                throw AppRequestException("Request is not awaiting confirmation.")
            }
            val history = RequestFirestoreMapper.readHistory(snap).toMutableList().apply {
                add(RequestFirestoreMapper.buildHistoryEntry(RequestStatus.Completed.name, requesterId))
            }
            val updates = RequestFirestoreMapper.buildConfirmDeliveryUpdateMap(history)
            tx.update(ref, updates)
        }.await()
    }

    suspend fun cancel(requestId: String, uid: String, reason: String) {
        val ref = requests.document(requestId)
        firestore.runTransaction { tx ->
            val snap = tx.get(ref)
            if (!snap.exists()) throw AppRequestException("Request not found.")
            val status = RequestFirestoreMapper.toRequestStatus(snap.getString(FIELD_STATUS))
                ?: throw AppRequestException("Invalid status.")
            if (status == RequestStatus.Completed ||
                status == RequestStatus.Cancelled ||
                status == RequestStatus.Delivered
            ) {
                throw AppRequestException("This request can no longer be cancelled.")
            }
            val isRequester = snap.getString(FIELD_REQUESTER_ID) == uid
            val isRunner = snap.getString(FIELD_RUNNER_ID) == uid
            if (!isRequester && !isRunner) {
                throw AppRequestException("Not allowed to cancel.")
            }
            if (status == RequestStatus.Pending && !isRequester) {
                throw AppRequestException("Only the requester can cancel a pending request.")
            }
            val by = if (isRequester) CancelledBy.Requester.name else CancelledBy.Runner.name
            val history = RequestFirestoreMapper.readHistory(snap).toMutableList().apply {
                add(RequestFirestoreMapper.buildHistoryEntry(RequestStatus.Cancelled.name, uid))
            }
            val updates = RequestFirestoreMapper.buildCancelUpdateMap(reason, by, history)
            tx.update(ref, updates)
        }.await()
    }

    suspend fun submitRating(requestId: String, requesterId: String, rating: RequestRating) {
        val ref = requests.document(requestId)
        firestore.runTransaction { tx ->
            val snap = tx.get(ref)
            if (!snap.exists()) throw AppRequestException("Request not found.")
            if (snap.getString(FIELD_REQUESTER_ID) != requesterId) {
                throw AppRequestException("Only the requester can rate.")
            }
            if (snap.getString(FIELD_STATUS) != RequestStatus.Completed.name) {
                throw AppRequestException("Rate after the request is completed.")
            }
            if (snap.get(FIELD_RATING) != null) {
                throw AppRequestException("Already rated.")
            }
            if (rating.stars !in 1..5) {
                throw AppRequestException("Stars must be 1–5.")
            }
            val updates = RequestFirestoreMapper.buildRatingUpdateMap(rating)
            tx.update(ref, updates)
        }.await()
    }

    suspend fun get(requestId: String): Request? =
        RequestFirestoreMapper.toRequest(requests.document(requestId).get().await())

    fun observeRequest(requestId: String): Flow<Request?> = callbackFlow {
        val registration = requests.document(requestId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    handleListenErrorNullable(error)
                    return@addSnapshotListener
                }
                trySend(RequestFirestoreMapper.toRequest(snapshot))
            }
        awaitClose { registration.remove() }
    }

    /**
     * Equality-only query + client sort — avoids composite-index soft-fails that
     * previously emptied Home after Accept when indexes were not yet deployed.
     */
    fun observeByRequester(requesterId: String): Flow<List<Request>> = listQueryFlow(
        requests.whereEqualTo(FIELD_REQUESTER_ID, requesterId),
        sortByUpdatedDesc = true,
    )

    fun observePending(limit: Int): Flow<List<Request>> = listQueryFlow(
        requests.whereEqualTo(FIELD_STATUS, RequestStatus.Pending.name)
            .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .limit(limit.toLong()),
    )

    fun observeByRunner(runnerId: String): Flow<List<Request>> = listQueryFlow(
        requests.whereEqualTo(FIELD_RUNNER_ID, runnerId),
        sortByUpdatedDesc = true,
    )

    fun observeMessages(requestId: String): Flow<List<ChatMessage>> = callbackFlow {
        val registration = requests.document(requestId).collection(MESSAGES)
            .orderBy(FIELD_CREATED_AT, Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    if (!isSoftListenFailure(error)) close(error)
                    return@addSnapshotListener
                }
                trySend(
                    snapshot?.documents?.mapNotNull { RequestFirestoreMapper.toChatMessage(it, requestId) }.orEmpty(),
                )
            }
        awaitClose { registration.remove() }
    }

    suspend fun sendMessage(
        requestId: String,
        senderId: String,
        text: String,
        clientId: String,
        isRequester: Boolean,
    ) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) throw AppRequestException("Message is empty.")
        val batch = firestore.batch()
        val msgRef = requests.document(requestId).collection(MESSAGES).document()
        batch.set(msgRef, RequestFirestoreMapper.buildMessageMap(senderId, trimmed, clientId))
        val reqRef = requests.document(requestId)
        batch.set(
            reqRef,
            RequestFirestoreMapper.buildChatMetadataUpdateMap(trimmed, senderId, isRequester),
            SetOptions.merge(),
        )
        batch.commit().await()
    }

    suspend fun markChatRead(requestId: String, isRequester: Boolean) {
        val field = if (isRequester) FIELD_REQUESTER_UNREAD else FIELD_RUNNER_UNREAD
        requests.document(requestId).update(
            mapOf(
                field to 0,
                FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
            ),
        ).await()
    }

    private fun listQueryFlow(
        query: Query,
        sortByUpdatedDesc: Boolean = false,
    ): Flow<List<Request>> = callbackFlow {
        val registration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleListenError(error)
                return@addSnapshotListener
            }
            val mapped = snapshot?.documents?.mapNotNull { RequestFirestoreMapper.toRequest(it) }.orEmpty()
            trySend(
                if (sortByUpdatedDesc) {
                    mapped.sortedByDescending { it.updatedAtEpochMs }
                } else {
                    mapped
                },
            )
        }
        awaitClose { registration.remove() }
    }

    /**
     * Soft failures (missing index / brief permission blip) keep the listener alive
     * so Home can recover instead of permanently showing an empty feed.
     */
    private fun ProducerScope<List<Request>>.handleListenError(error: Exception) {
        trySend(emptyList())
        if (!isSoftListenFailure(error)) close(error)
    }

    private fun ProducerScope<Request?>.handleListenErrorNullable(error: Exception) {
        trySend(null)
        if (!isSoftListenFailure(error)) close(error)
    }

    private fun isSoftListenFailure(error: Exception): Boolean {
        if (error !is FirebaseFirestoreException) return false
        return when (error.code) {
            FirebaseFirestoreException.Code.PERMISSION_DENIED,
            FirebaseFirestoreException.Code.FAILED_PRECONDITION,
            FirebaseFirestoreException.Code.UNAVAILABLE,
            -> true
            else -> false
        }
    }

    companion object {
        const val COLLECTION = "requests"
        const val MESSAGES = "messages"
        const val FIELD_REQUEST_ID = "requestId"
        const val FIELD_REQUESTER_ID = "requesterId"
        const val FIELD_RUNNER_ID = "runnerId"
        const val FIELD_CATEGORY = "category"
        const val FIELD_STATUS = "status"
        const val FIELD_TITLE = "title"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_BUDGET = "budget"
        const val FIELD_DELIVERY_FEE = "deliveryFee"
        const val FIELD_PICKUP_LABEL = "pickupLabel"
        const val FIELD_DROPOFF_LABEL = "dropoffLabel"
        const val FIELD_PICKUP_LATITUDE = "pickupLatitude"
        const val FIELD_PICKUP_LONGITUDE = "pickupLongitude"
        const val FIELD_DROPOFF_LATITUDE = "dropoffLatitude"
        const val FIELD_DROPOFF_LONGITUDE = "dropoffLongitude"
        const val FIELD_SHOPPING = "shopping"
        const val FIELD_PICKUP = "pickup"
        const val FIELD_BILLS = "bills"
        const val FIELD_CUSTOM = "custom"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
        const val FIELD_REQUESTER_NAME = "requesterName"
        const val FIELD_RUNNER_NAME = "runnerName"
        const val FIELD_ACCEPTED_AT = "acceptedAt"
        const val FIELD_STARTED_AT = "startedAt"
        const val FIELD_PURCHASED_AT = "purchasedAt"
        const val FIELD_ON_THE_WAY_AT = "onTheWayAt"
        const val FIELD_DELIVERED_AT = "deliveredAt"
        const val FIELD_COMPLETED_AT = "completedAt"
        const val FIELD_CANCELLED_AT = "cancelledAt"
        const val FIELD_CANCEL_REASON = "cancelReason"
        const val FIELD_CANCELLED_BY = "cancelledBy"
        const val FIELD_STATUS_HISTORY = "statusHistory"
        const val FIELD_LAST_MESSAGE = "lastMessage"
        const val FIELD_LAST_MESSAGE_AT = "lastMessageAt"
        const val FIELD_LAST_MESSAGE_SENDER = "lastMessageSenderId"
        const val FIELD_REQUESTER_UNREAD = "requesterUnreadCount"
        const val FIELD_RUNNER_UNREAD = "runnerUnreadCount"
        const val FIELD_RATING = "rating"
    }
}

class AppRequestException(message: String) : Exception(message)
