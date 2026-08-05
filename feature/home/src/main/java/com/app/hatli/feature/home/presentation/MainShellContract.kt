package com.app.hatli.feature.home.presentation

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

data class MainShellUiState(
    val isLoadingProfile: Boolean = true,
    val role: UserRole? = null,
    val selectedTab: MainTab = MainTab.Home,
    val requesterHome: RequesterHomeSnapshot? = null,
    val runnerHome: RunnerHomeSnapshot? = null,
    val allRequests: List<RequestSummary> = emptyList(),
    val allJobs: List<RequestSummary> = emptyList(),
    val requestFilter: RequestListFilter = RequestListFilter.Active,
    val jobFilter: JobListFilter = JobListFilter.Active,
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
                    RequestStatus.SearchingRunner,
                    RequestStatus.Accepted,
                    RequestStatus.InProgress,
                    RequestStatus.Draft,
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
}

sealed interface MainShellUiEvent : UiEvent {
    data class TabSelected(val tab: MainTab) : MainShellUiEvent
    data object CreateRequest : MainShellUiEvent
    data object OpenNotifications : MainShellUiEvent
    data object ToggleRunnerOnline : MainShellUiEvent
    data object ViewAllJobs : MainShellUiEvent
    data class RequestFilterSelected(val filter: RequestListFilter) : MainShellUiEvent
    data class JobFilterSelected(val filter: JobListFilter) : MainShellUiEvent
    data class QuickActionClicked(val id: String) : MainShellUiEvent
}

sealed interface MainShellUiEffect : UiEffect {
    data object NavigateCreateRequestStub : MainShellUiEffect
    data class ShowMessage(val message: String) : MainShellUiEffect
}
