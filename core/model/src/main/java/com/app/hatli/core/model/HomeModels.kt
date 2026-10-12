package com.app.hatli.core.model

/**
 * Lightweight request/job card for Home and list shells (Phase 2).
 */
data class RequestSummary(
    val id: String,
    val title: String,
    val status: RequestStatus,
    val category: RequestCategory? = null,
    val etaMinutes: Int? = null,
    val distanceMeters: Int? = null,
    val offerAmount: Double? = null,
    val runnerName: String? = null,
    val requesterName: String? = null,
    val unreadCount: Int = 0,
    val lastMessage: String? = null,
    val hasRating: Boolean = false,
    val updatedAtEpochMs: Long = 0L,
) {
    val needsRating: Boolean
        get() = status == RequestStatus.Completed && !hasRating

    val canOpenChat: Boolean
        get() = status != RequestStatus.Pending && status != RequestStatus.Cancelled
}

data class QuickAction(
    val id: String,
    val labelEn: String,
    val labelAr: String,
)

data class RequesterHomeSnapshot(
    val neighborhood: String = "",
    val active: RequestSummary? = null,
    val recent: List<RequestSummary> = emptyList(),
    val quickActions: List<QuickAction> = emptyList(),
    val tipEn: String = "",
    val tipAr: String = "",
    val unreadNotifications: Int = 0,
)

data class RunnerPresence(
    val isOnline: Boolean,
)

data class RunnerHomeStats(
    val dailyEarnings: Double,
    val todayJobsCount: Int,
    val rating: Double,
)

data class RunnerHomeSnapshot(
    val presence: RunnerPresence,
    val stats: RunnerHomeStats,
    val nearby: List<RequestSummary> = emptyList(),
    val activeJob: RequestSummary? = null,
    val unreadNotifications: Int = 0,
)
