package com.app.hatli.core.data.request.mapper

import com.app.hatli.core.data.request.FirestoreRequestDataSource
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
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.core.model.ShoppingDetails
import com.app.hatli.core.model.StatusHistoryEntry
import com.app.hatli.core.model.toSummary
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import java.util.Date

/**
 * Metadata and timeline state for a Firestore request document.
 */
data class RequestMetadata(
    val acceptedAtEpochMs: Long? = null,
    val startedAtEpochMs: Long? = null,
    val purchasedAtEpochMs: Long? = null,
    val onTheWayAtEpochMs: Long? = null,
    val deliveredAtEpochMs: Long? = null,
    val completedAtEpochMs: Long? = null,
    val cancelledAtEpochMs: Long? = null,
    val cancelReason: String? = null,
    val cancelledBy: CancelledBy? = null,
    val statusHistory: List<StatusHistoryEntry> = emptyList(),
    val lastMessage: String? = null,
    val lastMessageAtEpochMs: Long? = null,
    val lastMessageSenderId: String? = null,
    val requesterUnreadCount: Int = 0,
    val runnerUnreadCount: Int = 0,
    val rating: RequestRating? = null,
    val createdAtEpochMs: Long = 0L,
    val updatedAtEpochMs: Long = 0L,
)

/**
 * Dedicated mapper for Firestore request documents, inputs, updates, and domain models.
 */
object RequestFirestoreMapper {

    // -------------------------------------------------------------------------
    // 1. Category Mapping
    // -------------------------------------------------------------------------

    fun toRequestCategory(raw: String?): RequestCategory? {
        if (raw == null) return null
        return runCatching { RequestCategory.valueOf(raw) }.getOrNull()
    }

    fun toRequestCategory(snapshot: DocumentSnapshot): RequestCategory? {
        if (!snapshot.exists()) return null
        return toRequestCategory(snapshot.getString(FirestoreRequestDataSource.FIELD_CATEGORY))
    }

    // -------------------------------------------------------------------------
    // 2. Status Mapping
    // -------------------------------------------------------------------------

    fun toRequestStatus(raw: String?): RequestStatus? {
        if (raw == null) return null
        return runCatching { RequestStatus.valueOf(raw) }.getOrNull()
    }

    fun toRequestStatus(snapshot: DocumentSnapshot): RequestStatus? {
        if (!snapshot.exists()) return null
        return toRequestStatus(snapshot.getString(FirestoreRequestDataSource.FIELD_STATUS))
    }

    // -------------------------------------------------------------------------
    // 3. Summary Mapping
    // -------------------------------------------------------------------------

    fun toRequestSummary(request: Request): RequestSummary = request.toSummary()

    fun toRequestSummary(snapshot: DocumentSnapshot): RequestSummary? =
        toRequest(snapshot)?.let { toRequestSummary(it) }

    // -------------------------------------------------------------------------
    // 4. Metadata Mapping
    // -------------------------------------------------------------------------

    fun toRequestMetadata(snapshot: DocumentSnapshot?): RequestMetadata {
        if (snapshot == null || !snapshot.exists()) return RequestMetadata()
        return RequestMetadata(
            acceptedAtEpochMs = readTime(snapshot, FirestoreRequestDataSource.FIELD_ACCEPTED_AT),
            startedAtEpochMs = readTime(snapshot, FirestoreRequestDataSource.FIELD_STARTED_AT),
            purchasedAtEpochMs = readTime(snapshot, FirestoreRequestDataSource.FIELD_PURCHASED_AT),
            onTheWayAtEpochMs = readTime(snapshot, FirestoreRequestDataSource.FIELD_ON_THE_WAY_AT),
            deliveredAtEpochMs = readTime(snapshot, FirestoreRequestDataSource.FIELD_DELIVERED_AT),
            completedAtEpochMs = readTime(snapshot, FirestoreRequestDataSource.FIELD_COMPLETED_AT),
            cancelledAtEpochMs = readTime(snapshot, FirestoreRequestDataSource.FIELD_CANCELLED_AT),
            cancelReason = snapshot.getString(FirestoreRequestDataSource.FIELD_CANCEL_REASON),
            cancelledBy = toCancelledBy(snapshot.getString(FirestoreRequestDataSource.FIELD_CANCELLED_BY)),
            statusHistory = toStatusHistory(readHistory(snapshot)),
            lastMessage = snapshot.getString(FirestoreRequestDataSource.FIELD_LAST_MESSAGE),
            lastMessageAtEpochMs = readTime(snapshot, FirestoreRequestDataSource.FIELD_LAST_MESSAGE_AT),
            lastMessageSenderId = snapshot.getString(FirestoreRequestDataSource.FIELD_LAST_MESSAGE_SENDER),
            requesterUnreadCount = (snapshot.getLong(FirestoreRequestDataSource.FIELD_REQUESTER_UNREAD) ?: 0L).toInt(),
            runnerUnreadCount = (snapshot.getLong(FirestoreRequestDataSource.FIELD_RUNNER_UNREAD) ?: 0L).toInt(),
            rating = toRating(snapshot.get(FirestoreRequestDataSource.FIELD_RATING)),
            createdAtEpochMs = readTime(snapshot, FirestoreRequestDataSource.FIELD_CREATED_AT) ?: 0L,
            updatedAtEpochMs = readTime(snapshot, FirestoreRequestDataSource.FIELD_UPDATED_AT) ?: 0L,
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun toRequestMetadata(data: Map<String, Any?>): RequestMetadata {
        return RequestMetadata(
            acceptedAtEpochMs = parseEpochMs(data[FirestoreRequestDataSource.FIELD_ACCEPTED_AT]),
            startedAtEpochMs = parseEpochMs(data[FirestoreRequestDataSource.FIELD_STARTED_AT]),
            purchasedAtEpochMs = parseEpochMs(data[FirestoreRequestDataSource.FIELD_PURCHASED_AT]),
            onTheWayAtEpochMs = parseEpochMs(data[FirestoreRequestDataSource.FIELD_ON_THE_WAY_AT]),
            deliveredAtEpochMs = parseEpochMs(data[FirestoreRequestDataSource.FIELD_DELIVERED_AT]),
            completedAtEpochMs = parseEpochMs(data[FirestoreRequestDataSource.FIELD_COMPLETED_AT]),
            cancelledAtEpochMs = parseEpochMs(data[FirestoreRequestDataSource.FIELD_CANCELLED_AT]),
            cancelReason = data[FirestoreRequestDataSource.FIELD_CANCEL_REASON] as? String,
            cancelledBy = toCancelledBy(data[FirestoreRequestDataSource.FIELD_CANCELLED_BY] as? String),
            statusHistory = toStatusHistory(readHistory(data[FirestoreRequestDataSource.FIELD_STATUS_HISTORY])),
            lastMessage = data[FirestoreRequestDataSource.FIELD_LAST_MESSAGE] as? String,
            lastMessageAtEpochMs = parseEpochMs(data[FirestoreRequestDataSource.FIELD_LAST_MESSAGE_AT]),
            lastMessageSenderId = data[FirestoreRequestDataSource.FIELD_LAST_MESSAGE_SENDER] as? String,
            requesterUnreadCount = ((data[FirestoreRequestDataSource.FIELD_REQUESTER_UNREAD] as? Number)?.toLong() ?: 0L).toInt(),
            runnerUnreadCount = ((data[FirestoreRequestDataSource.FIELD_RUNNER_UNREAD] as? Number)?.toLong() ?: 0L).toInt(),
            rating = toRating(data[FirestoreRequestDataSource.FIELD_RATING]),
            createdAtEpochMs = parseEpochMs(data[FirestoreRequestDataSource.FIELD_CREATED_AT]) ?: 0L,
            updatedAtEpochMs = parseEpochMs(data[FirestoreRequestDataSource.FIELD_UPDATED_AT]) ?: 0L,
        )
    }

    // -------------------------------------------------------------------------
    // 5. Full Request Mapping
    // -------------------------------------------------------------------------

    fun toRequest(snapshot: DocumentSnapshot?): Request? {
        if (snapshot == null || !snapshot.exists()) return null
        val category = toRequestCategory(snapshot) ?: return null
        val status = toRequestStatus(snapshot) ?: return null
        val metadata = toRequestMetadata(snapshot)
        return Request(
            id = snapshot.getString(FirestoreRequestDataSource.FIELD_REQUEST_ID) ?: snapshot.id,
            requesterId = snapshot.getString(FirestoreRequestDataSource.FIELD_REQUESTER_ID).orEmpty(),
            runnerId = snapshot.getString(FirestoreRequestDataSource.FIELD_RUNNER_ID),
            category = category,
            status = status,
            title = snapshot.getString(FirestoreRequestDataSource.FIELD_TITLE).orEmpty(),
            description = snapshot.getString(FirestoreRequestDataSource.FIELD_DESCRIPTION).orEmpty(),
            budget = snapshot.getDouble(FirestoreRequestDataSource.FIELD_BUDGET)
                ?: snapshot.getLong(FirestoreRequestDataSource.FIELD_BUDGET)?.toDouble(),
            deliveryFee = snapshot.getDouble(FirestoreRequestDataSource.FIELD_DELIVERY_FEE)
                ?: snapshot.getLong(FirestoreRequestDataSource.FIELD_DELIVERY_FEE)?.toDouble()
                ?: 0.0,
            pickupLabel = snapshot.getString(FirestoreRequestDataSource.FIELD_PICKUP_LABEL),
            dropoffLabel = snapshot.getString(FirestoreRequestDataSource.FIELD_DROPOFF_LABEL),
            pickupLatitude = snapshot.getDouble(FirestoreRequestDataSource.FIELD_PICKUP_LATITUDE)
                ?: snapshot.getLong(FirestoreRequestDataSource.FIELD_PICKUP_LATITUDE)?.toDouble(),
            pickupLongitude = snapshot.getDouble(FirestoreRequestDataSource.FIELD_PICKUP_LONGITUDE)
                ?: snapshot.getLong(FirestoreRequestDataSource.FIELD_PICKUP_LONGITUDE)?.toDouble(),
            dropoffLatitude = snapshot.getDouble(FirestoreRequestDataSource.FIELD_DROPOFF_LATITUDE)
                ?: snapshot.getLong(FirestoreRequestDataSource.FIELD_DROPOFF_LATITUDE)?.toDouble(),
            dropoffLongitude = snapshot.getDouble(FirestoreRequestDataSource.FIELD_DROPOFF_LONGITUDE)
                ?: snapshot.getLong(FirestoreRequestDataSource.FIELD_DROPOFF_LONGITUDE)?.toDouble(),
            shopping = toShoppingDetails(snapshot.get(FirestoreRequestDataSource.FIELD_SHOPPING)),
            pickup = toPickupDetails(snapshot.get(FirestoreRequestDataSource.FIELD_PICKUP)),
            bills = toBillsDetails(snapshot.get(FirestoreRequestDataSource.FIELD_BILLS)),
            custom = toCustomDetails(snapshot.get(FirestoreRequestDataSource.FIELD_CUSTOM)),
            requesterName = snapshot.getString(FirestoreRequestDataSource.FIELD_REQUESTER_NAME),
            runnerName = snapshot.getString(FirestoreRequestDataSource.FIELD_RUNNER_NAME),
            acceptedAtEpochMs = metadata.acceptedAtEpochMs,
            startedAtEpochMs = metadata.startedAtEpochMs,
            purchasedAtEpochMs = metadata.purchasedAtEpochMs,
            onTheWayAtEpochMs = metadata.onTheWayAtEpochMs,
            deliveredAtEpochMs = metadata.deliveredAtEpochMs,
            completedAtEpochMs = metadata.completedAtEpochMs,
            cancelledAtEpochMs = metadata.cancelledAtEpochMs,
            cancelReason = metadata.cancelReason,
            cancelledBy = metadata.cancelledBy,
            statusHistory = metadata.statusHistory,
            lastMessage = metadata.lastMessage,
            lastMessageAtEpochMs = metadata.lastMessageAtEpochMs,
            lastMessageSenderId = metadata.lastMessageSenderId,
            requesterUnreadCount = metadata.requesterUnreadCount,
            runnerUnreadCount = metadata.runnerUnreadCount,
            rating = metadata.rating,
            createdAtEpochMs = metadata.createdAtEpochMs,
            updatedAtEpochMs = metadata.updatedAtEpochMs,
        )
    }

    fun toRequest(data: Map<String, Any?>, documentId: String = ""): Request? {
        val categoryRaw = data[FirestoreRequestDataSource.FIELD_CATEGORY] as? String ?: return null
        val category = toRequestCategory(categoryRaw) ?: return null
        val statusRaw = data[FirestoreRequestDataSource.FIELD_STATUS] as? String ?: return null
        val status = toRequestStatus(statusRaw) ?: return null
        val metadata = toRequestMetadata(data)

        val id = (data[FirestoreRequestDataSource.FIELD_REQUEST_ID] as? String)?.takeIf { it.isNotBlank() } ?: documentId
        val budgetNumber = data[FirestoreRequestDataSource.FIELD_BUDGET] as? Number
        val deliveryFeeNumber = data[FirestoreRequestDataSource.FIELD_DELIVERY_FEE] as? Number
        val pickupLat = data[FirestoreRequestDataSource.FIELD_PICKUP_LATITUDE] as? Number
        val pickupLng = data[FirestoreRequestDataSource.FIELD_PICKUP_LONGITUDE] as? Number
        val dropoffLat = data[FirestoreRequestDataSource.FIELD_DROPOFF_LATITUDE] as? Number
        val dropoffLng = data[FirestoreRequestDataSource.FIELD_DROPOFF_LONGITUDE] as? Number

        return Request(
            id = id,
            requesterId = (data[FirestoreRequestDataSource.FIELD_REQUESTER_ID] as? String).orEmpty(),
            runnerId = data[FirestoreRequestDataSource.FIELD_RUNNER_ID] as? String,
            category = category,
            status = status,
            title = (data[FirestoreRequestDataSource.FIELD_TITLE] as? String).orEmpty(),
            description = (data[FirestoreRequestDataSource.FIELD_DESCRIPTION] as? String).orEmpty(),
            budget = budgetNumber?.toDouble(),
            deliveryFee = deliveryFeeNumber?.toDouble() ?: 0.0,
            pickupLabel = data[FirestoreRequestDataSource.FIELD_PICKUP_LABEL] as? String,
            dropoffLabel = data[FirestoreRequestDataSource.FIELD_DROPOFF_LABEL] as? String,
            pickupLatitude = pickupLat?.toDouble(),
            pickupLongitude = pickupLng?.toDouble(),
            dropoffLatitude = dropoffLat?.toDouble(),
            dropoffLongitude = dropoffLng?.toDouble(),
            shopping = toShoppingDetails(data[FirestoreRequestDataSource.FIELD_SHOPPING]),
            pickup = toPickupDetails(data[FirestoreRequestDataSource.FIELD_PICKUP]),
            bills = toBillsDetails(data[FirestoreRequestDataSource.FIELD_BILLS]),
            custom = toCustomDetails(data[FirestoreRequestDataSource.FIELD_CUSTOM]),
            requesterName = data[FirestoreRequestDataSource.FIELD_REQUESTER_NAME] as? String,
            runnerName = data[FirestoreRequestDataSource.FIELD_RUNNER_NAME] as? String,
            acceptedAtEpochMs = metadata.acceptedAtEpochMs,
            startedAtEpochMs = metadata.startedAtEpochMs,
            purchasedAtEpochMs = metadata.purchasedAtEpochMs,
            onTheWayAtEpochMs = metadata.onTheWayAtEpochMs,
            deliveredAtEpochMs = metadata.deliveredAtEpochMs,
            completedAtEpochMs = metadata.completedAtEpochMs,
            cancelledAtEpochMs = metadata.cancelledAtEpochMs,
            cancelReason = metadata.cancelReason,
            cancelledBy = metadata.cancelledBy,
            statusHistory = metadata.statusHistory,
            lastMessage = metadata.lastMessage,
            lastMessageAtEpochMs = metadata.lastMessageAtEpochMs,
            lastMessageSenderId = metadata.lastMessageSenderId,
            requesterUnreadCount = metadata.requesterUnreadCount,
            runnerUnreadCount = metadata.runnerUnreadCount,
            rating = metadata.rating,
            createdAtEpochMs = metadata.createdAtEpochMs,
            updatedAtEpochMs = metadata.updatedAtEpochMs,
        )
    }

    // -------------------------------------------------------------------------
    // 6. Sub-Details Helpers
    // -------------------------------------------------------------------------

    @Suppress("UNCHECKED_CAST")
    fun toShoppingDetails(raw: Any?): ShoppingDetails? {
        val m = raw as? Map<String, Any?> ?: return null
        val items = m["itemsText"] as? String ?: return null
        return ShoppingDetails(m["shopName"] as? String, items, m["notes"] as? String)
    }

    @Suppress("UNCHECKED_CAST")
    fun toPickupDetails(raw: Any?): PickupDetails? {
        val m = raw as? Map<String, Any?> ?: return null
        val d = m["itemDescription"] as? String ?: return null
        return PickupDetails(d)
    }

    @Suppress("UNCHECKED_CAST")
    fun toBillsDetails(raw: Any?): BillsDetails? {
        val m = raw as? Map<String, Any?> ?: return null
        val type = m["billType"] as? String ?: return null
        val ref = m["accountRef"] as? String ?: return null
        val amount = (m["amount"] as? Number)?.toDouble() ?: return null
        return BillsDetails(type, ref, amount)
    }

    fun toCustomDetails(raw: Any?): CustomDetails? {
        if (raw == null) return null
        return CustomDetails()
    }

    @Suppress("UNCHECKED_CAST")
    fun toRating(raw: Any?): RequestRating? {
        val m = raw as? Map<String, Any?> ?: return null
        val stars = (m["stars"] as? Number)?.toInt() ?: return null
        return RequestRating(
            stars = stars,
            comment = m["comment"] as? String,
            ratedAtEpochMs = 0L,
        )
    }

    fun toCancelledBy(raw: String?): CancelledBy? {
        if (raw == null) return null
        return runCatching { CancelledBy.valueOf(raw) }.getOrNull()
    }

    @Suppress("UNCHECKED_CAST")
    fun readHistory(snapshot: DocumentSnapshot?): List<Map<String, Any?>> {
        if (snapshot == null || !snapshot.exists()) return emptyList()
        val raw = snapshot.get(FirestoreRequestDataSource.FIELD_STATUS_HISTORY) as? List<*> ?: return emptyList()
        return raw.mapNotNull { it as? Map<String, Any?> }
    }

    @Suppress("UNCHECKED_CAST")
    fun readHistory(raw: Any?): List<Map<String, Any?>> {
        val list = raw as? List<*> ?: return emptyList()
        return list.mapNotNull { it as? Map<String, Any?> }
    }

    fun toStatusHistory(entries: List<Map<String, Any?>>): List<StatusHistoryEntry> {
        return entries.mapNotNull { entry ->
            val st = (entry["status"] as? String)?.let {
                runCatching { RequestStatus.valueOf(it) }.getOrNull()
            } ?: return@mapNotNull null
            val at = (entry["at"] as? Number)?.toLong() ?: 0L
            val by = entry["byUid"] as? String ?: ""
            StatusHistoryEntry(st, at, by)
        }
    }

    fun readTime(snapshot: DocumentSnapshot, field: String): Long? =
        snapshot.getTimestamp(field)?.toDate()?.time ?: snapshot.getLong(field)

    fun parseEpochMs(value: Any?): Long? = when (value) {
        is Timestamp -> value.toDate().time
        is Date -> value.time
        is Number -> value.toLong()
        else -> null
    }

    // -------------------------------------------------------------------------
    // 7. Creation & Chat Mapping
    // -------------------------------------------------------------------------

    fun toCreateRequestMap(
        input: CreateRequestInput,
        requestId: String,
        requesterId: String,
        requesterName: String?,
    ): Map<String, Any?> = buildMap {
        put(FirestoreRequestDataSource.FIELD_REQUEST_ID, requestId)
        put(FirestoreRequestDataSource.FIELD_REQUESTER_ID, requesterId)
        put(FirestoreRequestDataSource.FIELD_RUNNER_ID, null)
        put(FirestoreRequestDataSource.FIELD_CATEGORY, input.category.name)
        put(FirestoreRequestDataSource.FIELD_STATUS, RequestStatus.Pending.name)
        put(FirestoreRequestDataSource.FIELD_TITLE, input.title)
        put(FirestoreRequestDataSource.FIELD_DESCRIPTION, input.description)
        put(FirestoreRequestDataSource.FIELD_BUDGET, input.budget)
        put(FirestoreRequestDataSource.FIELD_DELIVERY_FEE, input.deliveryFee)
        put(FirestoreRequestDataSource.FIELD_PICKUP_LABEL, input.pickupLabel)
        put(FirestoreRequestDataSource.FIELD_DROPOFF_LABEL, input.dropoffLabel)
        put(FirestoreRequestDataSource.FIELD_PICKUP_LATITUDE, input.pickupLatitude)
        put(FirestoreRequestDataSource.FIELD_PICKUP_LONGITUDE, input.pickupLongitude)
        put(FirestoreRequestDataSource.FIELD_DROPOFF_LATITUDE, input.dropoffLatitude)
        put(FirestoreRequestDataSource.FIELD_DROPOFF_LONGITUDE, input.dropoffLongitude)
        put(FirestoreRequestDataSource.FIELD_SHOPPING, input.shopping?.let {
            mapOf("shopName" to it.shopName, "itemsText" to it.itemsText, "notes" to it.notes)
        })
        put(FirestoreRequestDataSource.FIELD_PICKUP, input.pickup?.let {
            mapOf("itemDescription" to it.itemDescription)
        })
        put(FirestoreRequestDataSource.FIELD_BILLS, input.bills?.let {
            mapOf("billType" to it.billType, "accountRef" to it.accountRef, "amount" to it.amount)
        })
        put(FirestoreRequestDataSource.FIELD_CUSTOM, input.custom?.let { mapOf("ok" to true) })
        put(FirestoreRequestDataSource.FIELD_REQUESTER_NAME, requesterName)
        put(FirestoreRequestDataSource.FIELD_REQUESTER_UNREAD, 0)
        put(FirestoreRequestDataSource.FIELD_RUNNER_UNREAD, 0)
        put(FirestoreRequestDataSource.FIELD_STATUS_HISTORY, emptyList<Any>())
        put(FirestoreRequestDataSource.FIELD_CREATED_AT, FieldValue.serverTimestamp())
        put(FirestoreRequestDataSource.FIELD_UPDATED_AT, FieldValue.serverTimestamp())
    }

    fun toChatMessage(snapshot: DocumentSnapshot, requestId: String): ChatMessage? {
        if (!snapshot.exists()) return null
        return ChatMessage(
            id = snapshot.id,
            requestId = requestId,
            senderId = snapshot.getString("senderId").orEmpty(),
            text = snapshot.getString("text").orEmpty(),
            createdAtEpochMs = readTime(snapshot, FirestoreRequestDataSource.FIELD_CREATED_AT) ?: 0L,
            clientId = snapshot.getString("clientId").orEmpty(),
        )
    }

    // -------------------------------------------------------------------------
    // 8. Update Payload Builders
    // -------------------------------------------------------------------------

    fun buildHistoryEntry(
        status: String,
        byUid: String,
        at: Long = System.currentTimeMillis(),
    ): Map<String, Any?> = mapOf(
        "status" to status,
        "at" to at,
        "byUid" to byUid,
    )

    fun buildAcceptUpdateMap(
        runnerId: String,
        runnerName: String?,
        requesterName: String?,
        history: List<Map<String, Any?>>,
    ): Map<String, Any?> = mapOf(
        FirestoreRequestDataSource.FIELD_RUNNER_ID to runnerId,
        FirestoreRequestDataSource.FIELD_RUNNER_NAME to runnerName,
        FirestoreRequestDataSource.FIELD_REQUESTER_NAME to requesterName,
        FirestoreRequestDataSource.FIELD_STATUS to RequestStatus.Accepted.name,
        FirestoreRequestDataSource.FIELD_ACCEPTED_AT to FieldValue.serverTimestamp(),
        FirestoreRequestDataSource.FIELD_STATUS_HISTORY to history,
        FirestoreRequestDataSource.FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
    )

    fun buildAdvanceStatusUpdateMap(
        toStatus: RequestStatus,
        history: List<Map<String, Any?>>,
    ): Map<String, Any> {
        val updates = mutableMapOf<String, Any>(
            FirestoreRequestDataSource.FIELD_STATUS to toStatus.name,
            FirestoreRequestDataSource.FIELD_STATUS_HISTORY to history,
            FirestoreRequestDataSource.FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
        )
        when (toStatus) {
            RequestStatus.InProgress -> updates[FirestoreRequestDataSource.FIELD_STARTED_AT] = FieldValue.serverTimestamp()
            RequestStatus.Purchased -> updates[FirestoreRequestDataSource.FIELD_PURCHASED_AT] = FieldValue.serverTimestamp()
            RequestStatus.OnTheWay -> updates[FirestoreRequestDataSource.FIELD_ON_THE_WAY_AT] = FieldValue.serverTimestamp()
            RequestStatus.Delivered -> updates[FirestoreRequestDataSource.FIELD_DELIVERED_AT] = FieldValue.serverTimestamp()
            else -> Unit
        }
        return updates
    }

    fun buildConfirmDeliveryUpdateMap(
        history: List<Map<String, Any?>>,
    ): Map<String, Any> = mapOf(
        FirestoreRequestDataSource.FIELD_STATUS to RequestStatus.Completed.name,
        FirestoreRequestDataSource.FIELD_COMPLETED_AT to FieldValue.serverTimestamp(),
        FirestoreRequestDataSource.FIELD_STATUS_HISTORY to history,
        FirestoreRequestDataSource.FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
    )

    fun buildCancelUpdateMap(
        reason: String,
        by: String,
        history: List<Map<String, Any?>>,
    ): Map<String, Any> = mapOf(
        FirestoreRequestDataSource.FIELD_STATUS to RequestStatus.Cancelled.name,
        FirestoreRequestDataSource.FIELD_CANCEL_REASON to reason,
        FirestoreRequestDataSource.FIELD_CANCELLED_BY to by,
        FirestoreRequestDataSource.FIELD_CANCELLED_AT to FieldValue.serverTimestamp(),
        FirestoreRequestDataSource.FIELD_STATUS_HISTORY to history,
        FirestoreRequestDataSource.FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
    )

    fun buildRatingUpdateMap(rating: RequestRating): Map<String, Any> = mapOf(
        FirestoreRequestDataSource.FIELD_RATING to mapOf(
            "stars" to rating.stars,
            "comment" to rating.comment,
            "ratedAt" to FieldValue.serverTimestamp(),
        ),
        FirestoreRequestDataSource.FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
    )

    fun buildMessageMap(
        senderId: String,
        text: String,
        clientId: String,
    ): Map<String, Any> = mapOf(
        "senderId" to senderId,
        "text" to text,
        "clientId" to clientId,
        FirestoreRequestDataSource.FIELD_CREATED_AT to FieldValue.serverTimestamp(),
    )

    fun buildChatMetadataUpdateMap(
        lastMessage: String,
        senderId: String,
        isRequester: Boolean,
    ): Map<String, Any> {
        val unreadField = if (isRequester) {
            FirestoreRequestDataSource.FIELD_RUNNER_UNREAD
        } else {
            FirestoreRequestDataSource.FIELD_REQUESTER_UNREAD
        }
        return mapOf(
            FirestoreRequestDataSource.FIELD_LAST_MESSAGE to lastMessage,
            FirestoreRequestDataSource.FIELD_LAST_MESSAGE_AT to FieldValue.serverTimestamp(),
            FirestoreRequestDataSource.FIELD_LAST_MESSAGE_SENDER to senderId,
            unreadField to FieldValue.increment(1),
            FirestoreRequestDataSource.FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
        )
    }
}

// -----------------------------------------------------------------------------
// Backward-compatible extension functions
// -----------------------------------------------------------------------------

fun DocumentSnapshot.readHistory(): List<Map<String, Any?>> =
    RequestFirestoreMapper.readHistory(this)

fun CreateRequestInput.toFirestoreMap(
    requestId: String,
    requesterId: String,
    requesterName: String?,
): Map<String, Any?> =
    RequestFirestoreMapper.toCreateRequestMap(this, requestId, requesterId, requesterName)

fun DocumentSnapshot.toRequest(): Request? =
    RequestFirestoreMapper.toRequest(this)

fun DocumentSnapshot.toRequestSummary(): RequestSummary? =
    RequestFirestoreMapper.toRequestSummary(this)

fun DocumentSnapshot.toChatMessage(requestId: String): ChatMessage? =
    RequestFirestoreMapper.toChatMessage(this, requestId)

fun DocumentSnapshot.readTime(field: String): Long? =
    RequestFirestoreMapper.readTime(this, field)
