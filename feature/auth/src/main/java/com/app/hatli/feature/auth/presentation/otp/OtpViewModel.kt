package com.app.hatli.feature.auth.presentation.otp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.domain.usecase.auth.ResendOtpUseCase
import com.app.hatli.core.domain.usecase.auth.VerifyOtpUseCase
import com.app.hatli.core.domain.usecase.user.GetPendingRoleUseCase
import com.app.hatli.core.domain.usecase.user.GetUserProfileUseCase
import com.app.hatli.core.domain.usecase.user.SetPendingRoleUseCase
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.model.VerificationStatus
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val verifyOtp: VerifyOtpUseCase,
    private val resendOtp: ResendOtpUseCase,
    private val getUserProfile: GetUserProfileUseCase,
    private val getPendingRole: GetPendingRoleUseCase,
    private val setPendingRole: SetPendingRoleUseCase,
) : BaseViewModel<OtpUiState, OtpUiEvent, OtpUiEffect>(
    OtpUiState(
        verificationId = URLDecoder.decode(
            checkNotNull(savedStateHandle["verificationId"]),
            Charsets.UTF_8.name(),
        ),
        phone = URLDecoder.decode(
            checkNotNull(savedStateHandle["phone"]),
            Charsets.UTF_8.name(),
        ),
    ),
) {
    private var timerJob: Job? = null
    private var verifyJob: Job? = null

    init {
        startTimer()
    }

    override fun handleEvent(event: OtpUiEvent) {
        when (event) {
            is OtpUiEvent.CodeChanged -> {
                val digits = event.value.filter { it.isDigit() }.take(6)
                setState { copy(code = digits, errorMessage = null) }
                if (digits.length == 6) onEvent(OtpUiEvent.Verify)
            }
            OtpUiEvent.Verify -> verify()
            OtpUiEvent.Resend -> resend()
            OtpUiEvent.Tick -> Unit
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        setState { copy(secondsLeft = 60, canResend = false) }
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                val next = uiState.value.secondsLeft - 1
                if (next <= 0) {
                    setState { copy(secondsLeft = 0, canResend = true) }
                    break
                } else {
                    setState { copy(secondsLeft = next) }
                }
            }
        }
    }

    private fun verify() {
        val state = uiState.value
        if (state.isLoading || verifyJob?.isActive == true) return
        if (state.code.length < 6) {
            val isAr = java.util.Locale.getDefault().language == "ar"
            setState {
                copy(
                    errorMessage = if (isAr) {
                        "يرجى إدخال رمز التحقق المكون من 6 أرقام."
                    } else {
                        "Please enter the 6-digit verification code."
                    }
                )
            }
            return
        }
        setState { copy(isLoading = true, errorMessage = null) }
        verifyJob = viewModelScope.launch {
            try {
                when (val result = verifyOtp(state.verificationId, state.code)) {
                    is Result.Success -> routeAfterLogin()
                    is Result.Error -> setState {
                        copy(isLoading = false, errorMessage = result.userMessage())
                    }
                    Result.Loading -> setState { copy(isLoading = true) }
                }
            } catch (t: Throwable) {
                setState { copy(isLoading = false, errorMessage = t.message) }
            }
        }
    }

    private suspend fun routeAfterLogin() {
        when (val profile = getUserProfile()) {
            is Result.Success -> {
                setState { copy(isLoading = false) }
                val data = profile.data
                when {
                    data != null && data.profileCompleted -> {
                        if (data.role == UserRole.Runner && data.verificationStatus != VerificationStatus.Approved) {
                            sendEffect(OtpUiEffect.GoRunnerApproval)
                        } else {
                            sendEffect(OtpUiEffect.GoHome)
                        }
                    }
                    data != null && !data.profileCompleted -> {
                        setPendingRole(data.role)
                        sendEffect(OtpUiEffect.GoCreateProfile)
                    }
                    else -> {
                        val pending = getPendingRole()
                        if (pending != null) {
                            sendEffect(OtpUiEffect.GoCreateProfile)
                        } else {
                            sendEffect(OtpUiEffect.GoRoleSelect)
                        }
                    }
                }
            }
            is Result.Error -> {
                setState { copy(isLoading = false) }
                val pending = getPendingRole()
                if (pending != null) {
                    sendEffect(OtpUiEffect.GoCreateProfile)
                } else {
                    sendEffect(OtpUiEffect.GoRoleSelect)
                }
            }
            Result.Loading -> Unit
        }
    }

    private fun resend() {
        val state = uiState.value
        if (!state.canResend || state.isLoading || verifyJob?.isActive == true) return
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            when (val result = resendOtp(state.phone)) {
                is Result.Success -> {
                    setState {
                        copy(
                            isLoading = false,
                            verificationId = result.data.verificationId,
                        )
                    }
                    startTimer()
                }
                is Result.Error -> setState {
                    copy(isLoading = false, errorMessage = result.userMessage())
                }
                Result.Loading -> Unit
            }
        }
    }
}
