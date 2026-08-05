package com.app.hatli.core.model

/**
 * Lightweight request/job card for Home and list shells (Phase 2).
 */
data class RequestSummary(
    val id: String,
    val title: String,
    val status: RequestStatus,
    val etaMinutes: Int? = null,
    val distanceMeters: Int? = null,
    val offerAmount: Double? = null,
    val runnerName: String? = null,
    val updatedAtEpochMs: Long = 0L,
)

data class QuickAction(
    val id: String,
    val labelEn: String,
    val labelAr: String,
)

data class RequesterHomeSnapshot(
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
