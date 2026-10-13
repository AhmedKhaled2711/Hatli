package com.app.hatli.feature.auth.presentation.splash

import androidx.lifecycle.viewModelScope
import com.app.hatli.core.domain.usecase.user.ResolveStartDestinationUseCase
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val resolveStartDestination: ResolveStartDestinationUseCase,
) : BaseViewModel<SplashUiState, SplashUiEvent, SplashUiEffect>(SplashUiState()) {

    init {
        onEvent(SplashUiEvent.Resolve)
    }

    override fun handleEvent(event: SplashUiEvent) {
        when (event) {
            SplashUiEvent.Resolve -> resolve()
        }
    }

    private fun resolve() {
        viewModelScope.launch {
            setState { copy(isResolving = true) }
            val startedAt = System.currentTimeMillis()
            val destination = resolveStartDestination()
            val elapsed = System.currentTimeMillis() - startedAt
            val remaining = MIN_SPLASH_MS - elapsed
            if (remaining > 0) delay(remaining)
            setState { copy(isResolving = false) }
            sendEffect(
                when (destination) {
                    ResolveStartDestinationUseCase.StartDestination.Onboarding ->
                        SplashUiEffect.GoOnboarding

                    ResolveStartDestinationUseCase.StartDestination.Start ->
                        SplashUiEffect.GoStart

                    ResolveStartDestinationUseCase.StartDestination.Login ->
                        SplashUiEffect.GoLogin

                    ResolveStartDestinationUseCase.StartDestination.RoleSelect ->
                        SplashUiEffect.GoRoleSelect

                    ResolveStartDestinationUseCase.StartDestination.CreateProfile ->
                        SplashUiEffect.GoCreateProfile

                    ResolveStartDestinationUseCase.StartDestination.Home ->
                        SplashUiEffect.GoHome
                },
            )
        }
    }

    private companion object {
        /** Minimum branded splash delay (300ms) for smooth visual transition without blocking startup. */
        const val MIN_SPLASH_MS = 300L
    }
}
