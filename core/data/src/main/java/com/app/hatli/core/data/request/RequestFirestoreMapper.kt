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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue

@Suppress("UNCHECKED_CAST")
internal fun DocumentSnapshot.readHistory(): List<Map<String, Any?>> {
    val raw = get(FirestoreRequestDataSource.FIELD_STATUS_HISTORY) as? List<*> ?: return emptyList()
    return raw.mapNotNull { it as? Map<String, Any?> }
}

internal fun CreateRequestInput.toFirestoreMap(
    requestId: String,
    requesterId: String,
    requesterName: String?,
): Map<String, Any?> = buildMap {
    put(FirestoreRequestDataSource.FIELD_REQUEST_ID, requestId)
    put(FirestoreRequestDataSource.FIELD_REQUESTER_ID, requesterId)
    put(FirestoreRequestDataSource.FIELD_RUNNER_ID, null)
    put(FirestoreRequestDataSource.FIELD_CATEGORY, category.name)
    put(FirestoreRequestDataSource.FIELD_STATUS, RequestStatus.Pending.name)
    put(FirestoreRequestDataSource.FIELD_TITLE, title)
    put(FirestoreRequestDataSource.FIELD_DESCRIPTION, description)
    put(FirestoreRequestDataSource.FIELD_BUDGET, budget)
    put(FirestoreRequestDataSource.FIELD_DELIVERY_FEE, deliveryFee)
    put(FirestoreRequestDataSource.FIELD_PICKUP_LABEL, pickupLabel)
    put(FirestoreRequestDataSource.FIELD_DROPOFF_LABEL, dropoffLabel)
    put(FirestoreRequestDataSource.FIELD_PICKUP_LATITUDE, pickupLatitude)
    put(FirestoreRequestDataSource.FIELD_PICKUP_LONGITUDE, pickupLongitude)
    put(FirestoreRequestDataSource.FIELD_DROPOFF_LATITUDE, dropoffLatitude)
    put(FirestoreRequestDataSource.FIELD_DROPOFF_LONGITUDE, dropoffLongitude)
    put(FirestoreRequestDataSource.FIELD_SHOPPING, shopping?.let {
        mapOf("shopName" to it.shopName, "itemsText" to it.itemsText, "notes" to it.notes)
    })
    put(FirestoreRequestDataSource.FIELD_PICKUP, pickup?.let {
        mapOf("itemDescription" to it.itemDescription)
    })
    put(FirestoreRequestDataSource.FIELD_BILLS, bills?.let {
        mapOf("billType" to it.billType, "accountRef" to it.accountRef, "amount" to it.amount)
    })
    put(FirestoreRequestDataSource.FIELD_CUSTOM, custom?.let { mapOf("ok" to true) })
    put(FirestoreRequestDataSource.FIELD_REQUESTER_NAME, requesterName)
    put(FirestoreRequestDataSource.FIELD_REQUESTER_UNREAD, 0)
    put(FirestoreRequestDataSource.FIELD_RUNNER_UNREAD, 0)
    put(FirestoreRequestDataSource.FIELD_STATUS_HISTORY, emptyList<Any>())
    put(FirestoreRequestDataSource.FIELD_CREATED_AT, FieldValue.serverTimestamp())
    put(FirestoreRequestDataSource.FIELD_UPDATED_AT, FieldValue.serverTimestamp())
}

@Suppress("UNCHECKED_CAST")
internal fun DocumentSnapshot.toRequest(): Request? {
    if (!exists()) return null
    val category = runCatching {
        RequestCategory.valueOf(getString(FirestoreRequestDataSource.FIELD_CATEGORY) ?: return null)
    }.getOrNull() ?: return null
    val status = runCatching {
        RequestStatus.valueOf(getString(FirestoreRequestDataSource.FIELD_STATUS) ?: return null)
    }.getOrNull() ?: return null
    return Request(
        id = getString(FirestoreRequestDataSource.FIELD_REQUEST_ID) ?: id,
        requesterId = getString(FirestoreRequestDataSource.FIELD_REQUESTER_ID).orEmpty(),
        runnerId = getString(FirestoreRequestDataSource.FIELD_RUNNER_ID),
        category = category,
        status = status,
        title = getString(FirestoreRequestDataSource.FIELD_TITLE).orEmpty(),
        description = getString(FirestoreRequestDataSource.FIELD_DESCRIPTION).orEmpty(),
        budget = getDouble(FirestoreRequestDataSource.FIELD_BUDGET)
            ?: getLong(FirestoreRequestDataSource.FIELD_BUDGET)?.toDouble(),
        deliveryFee = getDouble(FirestoreRequestDataSource.FIELD_DELIVERY_FEE)
            ?: getLong(FirestoreRequestDataSource.FIELD_DELIVERY_FEE)?.toDouble()
            ?: 0.0,
        pickupLabel = getString(FirestoreRequestDataSource.FIELD_PICKUP_LABEL),
        dropoffLabel = getString(FirestoreRequestDataSource.FIELD_DROPOFF_LABEL),
        pickupLatitude = getDouble(FirestoreRequestDataSource.FIELD_PICKUP_LATITUDE)
            ?: getLong(FirestoreRequestDataSource.FIELD_PICKUP_LATITUDE)?.toDouble(),
        pickupLongitude = getDouble(FirestoreRequestDataSource.FIELD_PICKUP_LONGITUDE)
            ?: getLong(FirestoreRequestDataSource.FIELD_PICKUP_LONGITUDE)?.toDouble(),
        dropoffLatitude = getDouble(FirestoreRequestDataSource.FIELD_DROPOFF_LATITUDE)
            ?: getLong(FirestoreRequestDataSource.FIELD_DROPOFF_LATITUDE)?.toDouble(),
        dropoffLongitude = getDouble(FirestoreRequestDataSource.FIELD_DROPOFF_LONGITUDE)
            ?: getLong(FirestoreRequestDataSource.FIELD_DROPOFF_LONGITUDE)?.toDouble(),
        shopping = (get(FirestoreRequestDataSource.FIELD_SHOPPING) as? Map<String, Any?>)?.let { m ->
            val items = m["itemsText"] as? String ?: return@let null
            ShoppingDetails(m["shopName"] as? String, items, m["notes"] as? String)
        },
        pickup = (get(FirestoreRequestDataSource.FIELD_PICKUP) as? Map<String, Any?>)?.let { m ->
            val d = m["itemDescription"] as? String ?: return@let null
            PickupDetails(d)
        },
        bills = (get(FirestoreRequestDataSource.FIELD_BILLS) as? Map<String, Any?>)?.let { m ->
            val type = m["billType"] as? String ?: return@let null
            val ref = m["accountRef"] as? String ?: return@let null
            val amount = (m["amount"] as? Number)?.toDouble() ?: return@let null
            BillsDetails(type, ref, amount)
        },
        custom = get(FirestoreRequestDataSource.FIELD_CUSTOM)?.let { CustomDetails() },
        requesterName = getString(FirestoreRequestDataSource.FIELD_REQUESTER_NAME),
        runnerName = getString(FirestoreRequestDataSource.FIELD_RUNNER_NAME),
        acceptedAtEpochMs = readTime(FirestoreRequestDataSource.FIELD_ACCEPTED_AT),
        startedAtEpochMs = readTime(FirestoreRequestDataSource.FIELD_STARTED_AT),
        purchasedAtEpochMs = readTime(FirestoreRequestDataSource.FIELD_PURCHASED_AT),
        onTheWayAtEpochMs = readTime(FirestoreRequestDataSource.FIELD_ON_THE_WAY_AT),
        deliveredAtEpochMs = readTime(FirestoreRequestDataSource.FIELD_DELIVERED_AT),
        completedAtEpochMs = readTime(FirestoreRequestDataSource.FIELD_COMPLETED_AT),
        cancelledAtEpochMs = readTime(FirestoreRequestDataSource.FIELD_CANCELLED_AT),
        cancelReason = getString(FirestoreRequestDataSource.FIELD_CANCEL_REASON),
        cancelledBy = getString(FirestoreRequestDataSource.FIELD_CANCELLED_BY)?.let {
            runCatching { CancelledBy.valueOf(it) }.getOrNull()
        },
        statusHistory = readHistory().mapNotNull { entry ->
            val st = (entry["status"] as? String)?.let {
                runCatching { RequestStatus.valueOf(it) }.getOrNull()
            } ?: return@mapNotNull null
            val at = (entry["at"] as? Number)?.toLong() ?: 0L
            val by = entry["byUid"] as? String ?: ""
            StatusHistoryEntry(st, at, by)
        },
        lastMessage = getString(FirestoreRequestDataSource.FIELD_LAST_MESSAGE),
        lastMessageAtEpochMs = readTime(FirestoreRequestDataSource.FIELD_LAST_MESSAGE_AT),
        lastMessageSenderId = getString(FirestoreRequestDataSource.FIELD_LAST_MESSAGE_SENDER),
        requesterUnreadCount = (getLong(FirestoreRequestDataSource.FIELD_REQUESTER_UNREAD) ?: 0L).toInt(),
        runnerUnreadCount = (getLong(FirestoreRequestDataSource.FIELD_RUNNER_UNREAD) ?: 0L).toInt(),
        rating = (get(FirestoreRequestDataSource.FIELD_RATING) as? Map<String, Any?>)?.let { m ->
            val stars = (m["stars"] as? Number)?.toInt() ?: return@let null
            RequestRating(
                stars = stars,
                comment = m["comment"] as? String,
                ratedAtEpochMs = 0L,
            )
        },
        createdAtEpochMs = readTime(FirestoreRequestDataSource.FIELD_CREATED_AT) ?: 0L,
        updatedAtEpochMs = readTime(FirestoreRequestDataSource.FIELD_UPDATED_AT) ?: 0L,
    )
}

internal fun DocumentSnapshot.readTime(field: String): Long? =
    getTimestamp(field)?.toDate()?.time ?: getLong(field)

internal fun DocumentSnapshot.toChatMessage(requestId: String): ChatMessage? {
    if (!exists()) return null
    return ChatMessage(
        id = id,
        requestId = requestId,
        senderId = getString("senderId").orEmpty(),
        text = getString("text").orEmpty(),
        createdAtEpochMs = readTime(FirestoreRequestDataSource.FIELD_CREATED_AT) ?: 0L,
        clientId = getString("clientId").orEmpty(),
    )
}
