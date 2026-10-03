package com.app.hatli.feature.auth.presentation.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.common.util.PhoneSanitizer
import com.app.hatli.core.domain.usecase.auth.SendOtpUseCase
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sendOtp: SendOtpUseCase,
) : BaseViewModel<LoginUiState, LoginUiEvent, LoginUiEffect>(LoginUiState()) {

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
        val rawPhone = uiState.value.phone
        val sanitized = PhoneSanitizer.sanitizeEgyptianPhoneNumber(rawPhone)
        
        Log.d(TAG, "Submitting phone auth. Raw input: '$rawPhone', Sanitized E.164: '$sanitized'")

        if (!PhoneSanitizer.isValidEgyptianPhoneNumber(sanitized)) {
            setState {
                copy(
                    isLoading = false,
                    errorMessage = "يرجى إدخال رقم هاتف مصري صحيح (مثال: 01007394856)",
                )
            }
            return
        }

        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
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
        }
    }
}
