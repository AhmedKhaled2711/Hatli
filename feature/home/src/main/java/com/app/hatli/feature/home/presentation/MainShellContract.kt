package com.app.hatli.feature.home.presentation

import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.core.model.RequesterHomeSnapshot
import com.app.hatli.core.model.RunnerHomeSnapshot
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

enum class MainTab {
    Home,
    RequestsOrJobs,
    Messages,
    Profile,
}

enum class RequestListFilter {
    Active,
    Completed,
    Cancelled,
}

enum class JobListFilter {
    Active,
    History,
}

enum class RunnerNextAction {
    StartWork,
    MarkPurchased,
    OnTheWay,
    MarkDelivered,
}

data class MainShellUiState(
    val isLoadingProfile: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val role: UserRole? = null,
    val selectedTab: MainTab = MainTab.Home,
    val requesterHome: RequesterHomeSnapshot? = null,
    val runnerHome: RunnerHomeSnapshot? = null,
    val allRequests: List<RequestSummary> = emptyList(),
    val allJobs: List<RequestSummary> = emptyList(),
    val requestFilter: RequestListFilter = RequestListFilter.Active,
    val jobFilter: JobListFilter = JobListFilter.Active,
    val showRunnerOnlineInfoBanner: Boolean = false,
) : UiState {
    val isRequester: Boolean get() = role == UserRole.Requester
    val isRunner: Boolean get() = role == UserRole.Runner
    val unreadNotifications: Int
        get() = when (role) {
            UserRole.Requester -> requesterHome?.unreadNotifications ?: 0
            UserRole.Runner -> runnerHome?.unreadNotifications ?: 0
            null -> 0
        }
    val showFab: Boolean get() = isRequester

    val filteredRequests: List<RequestSummary>
        get() = allRequests.filter { summary ->
            when (requestFilter) {
                RequestListFilter.Active -> summary.status in setOf(
                    RequestStatus.Pending,
                    RequestStatus.Accepted,
                    RequestStatus.InProgress,
                    RequestStatus.Purchased,
                    RequestStatus.OnTheWay,
                    RequestStatus.Delivered,
                )
                RequestListFilter.Completed -> summary.status == RequestStatus.Completed
                RequestListFilter.Cancelled -> summary.status == RequestStatus.Cancelled
            }
        }

    val filteredJobs: List<RequestSummary>
        get() = allJobs.filter { summary ->
            when (jobFilter) {
                JobListFilter.Active -> summary.status != RequestStatus.Completed &&
                    summary.status != RequestStatus.Cancelled
                JobListFilter.History -> summary.status == RequestStatus.Completed ||
                    summary.status == RequestStatus.Cancelled
            }
        }

    /** Threads available for the Messages tab (accepted+ work with chat). */
    val conversations: List<RequestSummary>
        get() {
            val source = when (role) {
                UserRole.Requester -> allRequests
                UserRole.Runner -> allJobs.filter { it.status != RequestStatus.Pending }
                null -> emptyList()
            }
            return source
                .filter { it.canOpenChat }
                .sortedWith(
                    compareByDescending<RequestSummary> { it.unreadCount > 0 }
                        .thenByDescending { it.updatedAtEpochMs },
                )
        }
}

sealed interface MainShellUiEvent : UiEvent {
    data object Retry : MainShellUiEvent
    data class TabSelected(val tab: MainTab) : MainShellUiEvent
    data object CreateRequest : MainShellUiEvent
    data object OpenNotifications : MainShellUiEvent
    data object ToggleRunnerOnline : MainShellUiEvent
    data object DismissRunnerOnlineInfoBanner : MainShellUiEvent
    data object ViewAllJobs : MainShellUiEvent
    data class RequestFilterSelected(val filter: RequestListFilter) : MainShellUiEvent
    data class JobFilterSelected(val filter: JobListFilter) : MainShellUiEvent
    data class QuickActionClicked(val id: String) : MainShellUiEvent
    data class AcceptRequest(val requestId: String) : MainShellUiEvent
    data class AdvanceStatus(val requestId: String, val toStatus: RequestStatus) : MainShellUiEvent
    data class ConfirmDelivery(val requestId: String) : MainShellUiEvent
    data class OpenChat(val requestId: String) : MainShellUiEvent
    data class OpenDetail(val requestId: String) : MainShellUiEvent
    data class OpenRate(val requestId: String) : MainShellUiEvent
}

sealed interface MainShellUiEffect : UiEffect {
    data class NavigateCreateRequest(val initialCategory: String? = null) : MainShellUiEffect
    data class NavigateChat(val requestId: String) : MainShellUiEffect
    data class NavigateDetail(val requestId: String) : MainShellUiEffect
    data class NavigateRate(val requestId: String) : MainShellUiEffect
    data class ShowMessage(val message: String) : MainShellUiEffect
    data object NavigateRunnerApproval : MainShellUiEffect
}

fun RequestSummary.runnerNextStatus(): RequestStatus? {
    // Mirror Request.nextRunnerStatus using summary fields.
    return when (status) {
        RequestStatus.Accepted -> RequestStatus.InProgress
        RequestStatus.InProgress -> when (category) {
            RequestCategory.Shopping -> RequestStatus.Purchased
            else -> RequestStatus.OnTheWay
        }
        RequestStatus.Purchased -> RequestStatus.OnTheWay
        RequestStatus.OnTheWay -> RequestStatus.Delivered
        else -> null
    }
}

fun RequestSummary.runnerNextAction(): RunnerNextAction? {
    return when (runnerNextStatus()) {
        RequestStatus.InProgress -> RunnerNextAction.StartWork
        RequestStatus.Purchased -> RunnerNextAction.MarkPurchased
        RequestStatus.OnTheWay -> RunnerNextAction.OnTheWay
        RequestStatus.Delivered -> RunnerNextAction.MarkDelivered
        null -> null
        else -> null
    }
}
