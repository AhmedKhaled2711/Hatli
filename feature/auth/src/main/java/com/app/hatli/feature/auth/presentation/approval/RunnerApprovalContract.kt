package com.app.hatli.feature.auth.presentation.approval

import com.app.hatli.core.model.TransportType
import com.app.hatli.core.model.VerificationStatus
import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class RunnerApprovalUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val status: VerificationStatus? = null,
    val runnerName: String = "",
    val phone: String = "",
    val transportType: TransportType? = null,
    val vehicleInfo: String? = null,
    val lastCheckedEpochMs: Long? = null,
    val errorMessage: String? = null,
) : UiState

sealed interface RunnerApprovalUiEvent : UiEvent {
    data object Refresh : RunnerApprovalUiEvent
    data object Logout : RunnerApprovalUiEvent
    data object ClearError : RunnerApprovalUiEvent
}

sealed interface RunnerApprovalUiEffect : UiEffect {
    data object GoHome : RunnerApprovalUiEffect
    data object GoAuth : RunnerApprovalUiEffect
    data class ShowMessage(val message: String) : RunnerApprovalUiEffect
}
