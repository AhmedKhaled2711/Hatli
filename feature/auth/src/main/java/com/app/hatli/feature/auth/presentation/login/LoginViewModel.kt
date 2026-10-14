package com.app.hatli.feature.auth.presentation.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.common.util.PhoneSanitizer
import com.app.hatli.core.domain.usecase.auth.SendOtpUseCase
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sendOtp: SendOtpUseCase,
) : BaseViewModel<LoginUiState, LoginUiEvent, LoginUiEffect>(LoginUiState()) {

    private var submitJob: Job? = null

    override fun handleEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.PhoneChanged -> {
                val input = event.value
                val isValid = PhoneSanitizer.isValidEgyptianPhoneNumber(input)
                setState {
                    copy(
                        phone = input,
                        errorMessage = null,
                    )
                }
            }
            LoginUiEvent.Submit -> submit()
        }
    }

    private fun submit() {
        if (uiState.value.isLoading || submitJob?.isActive == true) return
        val rawPhone = uiState.value.phone
        val sanitized = PhoneSanitizer.sanitizeEgyptianPhoneNumber(rawPhone)

        Log.d(TAG, "Submitting phone auth request")

        if (!PhoneSanitizer.isValidEgyptianPhoneNumber(sanitized)) {
            val isAr = java.util.Locale.getDefault().language == "ar"
            setState {
                copy(
                    isLoading = false,
                    errorMessage = if (isAr) {
                        "يرجى إدخال رقم هاتف مصري صحيح (مثال: 01007394856)"
                    } else {
                        "Please enter a valid Egyptian phone number (e.g. 01007394856)"
                    },
                )
            }
            return
        }

        setState { copy(isLoading = true, errorMessage = null) }
        submitJob = viewModelScope.launch {
            try {
                when (val result = sendOtp(sanitized)) {
                    is Result.Success -> {
                        Log.d(TAG, "sendOtp success! Verification ID: ${result.data.verificationId}")
                        setState { copy(isLoading = false) }
                        sendEffect(
                            LoginUiEffect.GoOtp(
                                verificationId = result.data.verificationId,
                                phone = result.data.phoneNumber,
                            ),
                        )
                    }
                    is Result.Error -> {
                        val msg = result.userMessage()
                        Log.e(TAG, "sendOtp failed: $msg", result.exception)
                        setState {
                            copy(isLoading = false, errorMessage = msg)
                        }
                    }
                    Result.Loading -> setState { copy(isLoading = true) }
                }
            } catch (t: Throwable) {
                Log.e(TAG, "sendOtp threw unexpected exception: ${t.message}", t)
                setState { copy(isLoading = false, errorMessage = t.message) }
            }
        }
    }
}
