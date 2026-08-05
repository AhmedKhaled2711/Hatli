package com.app.hatli.core.model

/**
 * Marketplace request lifecycle.
 * Create → Pending; Accept → Accepted; then Runner advances to Delivered;
 * Requester confirms → Completed. Non-Shopping skips Purchased.
 */
enum class RequestStatus {
    Pending,
    Accepted,
    InProgress,
    Purchased,
    OnTheWay,
    Delivered,
    Completed,
    Cancelled,
}

enum class CancelledBy {
    Requester,
    Runner,
}

data class StatusHistoryEntry(
    val status: RequestStatus,
    val atEpochMs: Long,
    val byUid: String,
)

data class RequestRating(
    val stars: Int,
    val comment: String? = null,
    val ratedAtEpochMs: Long = 0L,
)

data class ChatMessage(
    val id: String,
    val requestId: String,
    val senderId: String,
    val text: String,
    val createdAtEpochMs: Long = 0L,
    val clientId: String = "",
)
