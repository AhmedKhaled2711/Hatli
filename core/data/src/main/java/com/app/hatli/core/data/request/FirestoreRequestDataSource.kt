package com.app.hatli.core.data.request

import com.app.hatli.core.model.BillsDetails
import com.app.hatli.core.model.CancelledBy
import com.app.hatli.core.model.ChatMessage
import com.app.hatli.core.model.CreateRequestInput
import com.app.hatli.core.model.CustomDetails
import com.app.hatli.core.model.PickupDetails
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestRating
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.ShoppingDetails
import com.app.hatli.core.model.StatusHistoryEntry
import com.app.hatli.core.model.nextRunnerStatus
import com.google.firebase.firestore.DocumentSnapshot
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
        val data = input.toFirestoreMap(
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
            val history = snap.readHistory().toMutableList()
            history.add(
                mapOf(
                    "status" to RequestStatus.Accepted.name,
                    "at" to System.currentTimeMillis(),
                    "byUid" to runnerId,
                ),
            )
            tx.update(
                ref,
                mapOf(
                    FIELD_RUNNER_ID to runnerId,
                    FIELD_RUNNER_NAME to runnerName,
                    FIELD_REQUESTER_NAME to (
                        snap.getString(FIELD_REQUESTER_NAME) ?: requesterNameFallback
                        ),
                    FIELD_STATUS to RequestStatus.Accepted.name,
                    FIELD_ACCEPTED_AT to FieldValue.serverTimestamp(),
                    FIELD_STATUS_HISTORY to history,
                    FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
                ),
            )
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
            val current = RequestStatus.valueOf(
                snap.getString(FIELD_STATUS) ?: throw AppRequestException("Invalid status."),
            )
            val category = RequestCategory.valueOf(
                snap.getString(FIELD_CATEGORY) ?: RequestCategory.Custom.name,
            )
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
            val history = snap.readHistory().toMutableList()
            history.add(
                mapOf(
                    "status" to toStatus.name,
                    "at" to System.currentTimeMillis(),
                    "byUid" to uid,
                ),
            )
            val updates = mutableMapOf<String, Any>(
                FIELD_STATUS to toStatus.name,
                FIELD_STATUS_HISTORY to history,
                FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
            )
            when (toStatus) {
                RequestStatus.InProgress -> updates[FIELD_STARTED_AT] = FieldValue.serverTimestamp()
                RequestStatus.Purchased -> updates[FIELD_PURCHASED_AT] = FieldValue.serverTimestamp()
                RequestStatus.OnTheWay -> updates[FIELD_ON_THE_WAY_AT] = FieldValue.serverTimestamp()
                RequestStatus.Delivered -> updates[FIELD_DELIVERED_AT] = FieldValue.serverTimestamp()
                else -> Unit
            }
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
            val history = snap.readHistory().toMutableList()
            history.add(
                mapOf(
                    "status" to RequestStatus.Completed.name,
                    "at" to System.currentTimeMillis(),
                    "byUid" to requesterId,
                ),
            )
            tx.update(
                ref,
                mapOf(
                    FIELD_STATUS to RequestStatus.Completed.name,
                    FIELD_COMPLETED_AT to FieldValue.serverTimestamp(),
                    FIELD_STATUS_HISTORY to history,
                    FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
                ),
            )
        }.await()
    }

    suspend fun cancel(requestId: String, uid: String, reason: String) {
        val ref = requests.document(requestId)
        firestore.runTransaction { tx ->
            val snap = tx.get(ref)
            if (!snap.exists()) throw AppRequestException("Request not found.")
            val status = RequestStatus.valueOf(
                snap.getString(FIELD_STATUS) ?: throw AppRequestException("Invalid status."),
            )
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
            val history = snap.readHistory().toMutableList()
            history.add(
                mapOf(
                    "status" to RequestStatus.Cancelled.name,
                    "at" to System.currentTimeMillis(),
                    "byUid" to uid,
                ),
            )
            tx.update(
                ref,
                mapOf(
                    FIELD_STATUS to RequestStatus.Cancelled.name,
                    FIELD_CANCEL_REASON to reason,
                    FIELD_CANCELLED_BY to by,
                    FIELD_CANCELLED_AT to FieldValue.serverTimestamp(),
                    FIELD_STATUS_HISTORY to history,
                    FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
                ),
            )
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
            tx.update(
                ref,
                mapOf(
                    FIELD_RATING to mapOf(
                        "stars" to rating.stars,
                        "comment" to rating.comment,
                        "ratedAt" to FieldValue.serverTimestamp(),
                    ),
                    FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
                ),
            )
        }.await()
    }

    suspend fun get(requestId: String): Request? =
        requests.document(requestId).get().await().toRequest()

    fun observeRequest(requestId: String): Flow<Request?> = callbackFlow {
        val registration = requests.document(requestId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    handleListenErrorNullable(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toRequest())
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
                    snapshot?.documents?.mapNotNull { it.toChatMessage(requestId) }.orEmpty(),
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
        batch.set(
            msgRef,
            mapOf(
                "senderId" to senderId,
                "text" to trimmed,
                "clientId" to clientId,
                FIELD_CREATED_AT to FieldValue.serverTimestamp(),
            ),
        )
        val reqRef = requests.document(requestId)
        val unreadField = if (isRequester) FIELD_RUNNER_UNREAD else FIELD_REQUESTER_UNREAD
        batch.set(
            reqRef,
            mapOf(
                FIELD_LAST_MESSAGE to trimmed,
                FIELD_LAST_MESSAGE_AT to FieldValue.serverTimestamp(),
                FIELD_LAST_MESSAGE_SENDER to senderId,
                unreadField to FieldValue.increment(1),
                FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
            ),
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
            val mapped = snapshot?.documents?.mapNotNull { it.toRequest() }.orEmpty()
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
