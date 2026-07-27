package com.app.hatli.feature.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.domain.usecase.auth.SendOtpUseCase
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sendOtp: SendOtpUseCase,
) : BaseViewModel<LoginUiState, LoginUiEvent, LoginUiEffect>(LoginUiState()) {

    override fun handleEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.PhoneChanged ->
                setState { copy(phone = event.value, errorMessage = null) }
            LoginUiEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val phone = uiState.value.phone
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            when (val result = sendOtp(phone)) {
                is Result.Success -> {
                    setState { copy(isLoading = false) }
                    sendEffect(
                        LoginUiEffect.GoOtp(
                            verificationId = result.data.verificationId,
                            phone = result.data.phoneNumber,
                        ),
                    )
                }
                is Result.Error -> setState {
                    copy(isLoading = false, errorMessage = result.userMessage())
                }
                Result.Loading -> setState { copy(isLoading = true) }
            }
        }
    }
}
