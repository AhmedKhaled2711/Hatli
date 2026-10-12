package com.app.hatli.core.model

/** Top-level request categories (places like butcher/bakery are details, not categories). */
enum class RequestCategory {
    Shopping,
    Pickup,
    Bills,
    Custom,
}

data class ShoppingDetails(
    val shopName: String? = null,
    val itemsText: String,
    val notes: String? = null,
)

data class PickupDetails(
    val itemDescription: String,
)

data class BillsDetails(
    val billType: String,
    val accountRef: String,
    val amount: Double,
)

data class CustomDetails(
    val unused: Boolean = true,
)

/**
 * Full marketplace request (Firestore `requests/{id}`).
 */
data class Request(
    val id: String,
    val requesterId: String,
    val runnerId: String? = null,
    val category: RequestCategory,
    val status: RequestStatus,
    val title: String,
    val description: String,
    val budget: Double? = null,
    val deliveryFee: Double,
    val pickupLabel: String? = null,
    val dropoffLabel: String? = null,
    val pickupLatitude: Double? = null,
    val pickupLongitude: Double? = null,
    val dropoffLatitude: Double? = null,
    val dropoffLongitude: Double? = null,
    val shopping: ShoppingDetails? = null,
    val pickup: PickupDetails? = null,
    val bills: BillsDetails? = null,
    val custom: CustomDetails? = null,
    val requesterName: String? = null,
    val runnerName: String? = null,
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
) {
    val isTerminal: Boolean
        get() = status == RequestStatus.Completed || status == RequestStatus.Cancelled

    val isActiveWork: Boolean
        get() = status != RequestStatus.Pending && !isTerminal
}

/** Input for creating a Pending request (no id / timestamps / runner). */
data class CreateRequestInput(
    val category: RequestCategory,
    val title: String,
    val description: String,
    val budget: Double? = null,
    val deliveryFee: Double,
    val pickupLabel: String? = null,
    val dropoffLabel: String? = null,
    val pickupLatitude: Double? = null,
    val pickupLongitude: Double? = null,
    val dropoffLatitude: Double? = null,
    val dropoffLongitude: Double? = null,
    val shopping: ShoppingDetails? = null,
    val pickup: PickupDetails? = null,
    val bills: BillsDetails? = null,
    val custom: CustomDetails? = null,
    val requesterName: String? = null,
)

/** Default suggested delivery fee (EGP) until maps/distance pricing. */
object RequestDefaults {
    const val SUGGESTED_DELIVERY_FEE_EGP: Double = 35.0
}

/**
 * Next legal wire status for the Runner (or null if none / Requester must confirm).
 */
fun Request.nextRunnerStatus(): RequestStatus? = when (status) {
    RequestStatus.Accepted -> RequestStatus.InProgress
    RequestStatus.InProgress -> when (category) {
        RequestCategory.Shopping -> RequestStatus.Purchased
        else -> RequestStatus.OnTheWay
    }
    RequestStatus.Purchased -> RequestStatus.OnTheWay
    RequestStatus.OnTheWay -> RequestStatus.Delivered
    else -> null
}

fun RequestStatus.requesterLabelAr(runnerName: String?): String = when (this) {
    RequestStatus.Pending -> "جاري البحث عن Runner"
    RequestStatus.Accepted -> "${runnerName ?: "Runner"} قبل طلبك"
    RequestStatus.InProgress -> "جاري التنفيذ"
    RequestStatus.Purchased -> "تم الشراء"
    RequestStatus.OnTheWay -> "في الطريق"
    RequestStatus.Delivered -> "تم التسليم — أكّد الاستلام"
    RequestStatus.Completed -> "مكتمل"
    RequestStatus.Cancelled -> "ملغي"
}

fun RequestStatus.runnerCtaLabel(): String? = when (this) {
    RequestStatus.Accepted -> "Start work"
    RequestStatus.InProgress -> "Mark purchased" // Shopping; UI overrides for non-shopping
    RequestStatus.Purchased -> "On the way"
    RequestStatus.OnTheWay -> "Mark delivered"
    RequestStatus.Delivered -> null
    else -> null
}

fun Request.toSummary(): RequestSummary = RequestSummary(
    id = id,
    title = title,
    status = status,
    category = category,
    offerAmount = deliveryFee,
    runnerName = runnerName,
    requesterName = requesterName,
    unreadCount = maxOf(requesterUnreadCount, runnerUnreadCount),
    lastMessage = lastMessage,
    hasRating = rating != null,
    updatedAtEpochMs = updatedAtEpochMs,
    createdAtEpochMs = createdAtEpochMs,
    pickupLabel = pickupLabel,
    dropoffLabel = dropoffLabel,
    description = description,
)

/**
 * Visible lifecycle steps for UI steppers (skips Purchased for non-Shopping).
 */
fun lifecycleSteps(category: RequestCategory?): List<RequestStatus> {
    val mid = buildList {
        add(RequestStatus.Accepted)
        add(RequestStatus.InProgress)
        if (category == null || category == RequestCategory.Shopping) {
            add(RequestStatus.Purchased)
        }
        add(RequestStatus.OnTheWay)
        add(RequestStatus.Delivered)
        add(RequestStatus.Completed)
    }
    return mid
}

fun RequestStatus.stepperIndex(category: RequestCategory?): Int {
    if (this == RequestStatus.Pending) return -1
    if (this == RequestStatus.Cancelled) return -1
    val steps = lifecycleSteps(category)
    val idx = steps.indexOf(this)
    if (idx >= 0) return idx
    // Purchased skipped for non-shopping — map onto OnTheWay slot.
    return when (this) {
        RequestStatus.Purchased -> steps.indexOf(RequestStatus.OnTheWay).coerceAtLeast(0)
        else -> 0
    }
}

fun RequestStatus.shortLabelEn(): String = when (this) {
    RequestStatus.Pending -> "Searching"
    RequestStatus.Accepted -> "Accepted"
    RequestStatus.InProgress -> "In progress"
    RequestStatus.Purchased -> "Purchased"
    RequestStatus.OnTheWay -> "On the way"
    RequestStatus.Delivered -> "Delivered"
    RequestStatus.Completed -> "Completed"
    RequestStatus.Cancelled -> "Cancelled"
}

fun RequestStatus.shortLabelAr(): String = when (this) {
    RequestStatus.Pending -> "بحث"
    RequestStatus.Accepted -> "مقبول"
    RequestStatus.InProgress -> "تنفيذ"
    RequestStatus.Purchased -> "شراء"
    RequestStatus.OnTheWay -> "في الطريق"
    RequestStatus.Delivered -> "تسليم"
    RequestStatus.Completed -> "مكتمل"
    RequestStatus.Cancelled -> "ملغي"
}
