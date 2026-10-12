package com.app.hatli.core.domain.usecase.user

import com.app.hatli.core.common.error.AppError
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.repository.AuthRepository
import com.app.hatli.core.domain.repository.SessionPreferencesRepository
import com.app.hatli.core.domain.repository.UserRepository
import com.app.hatli.core.model.CreateProfileInput
import com.app.hatli.core.model.UserProfile
import com.app.hatli.core.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<UserProfile?> {
        val uid = authRepository.currentUser()?.uid
            ?: return Result.Error(AppError.Unauthorized())
        return userRepository.getProfile(uid)
    }
}

class ObserveUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<UserProfile?> {
        val uid = authRepository.currentUser()?.uid ?: return kotlinx.coroutines.flow.flowOf(null)
        return userRepository.observeProfile(uid)
    }
}

class CreateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val sessionPreferences: SessionPreferencesRepository,
) {
    suspend operator fun invoke(input: CreateProfileInput): Result<UserProfile> {
        authRepository.currentUser()
            ?: return Result.Error(AppError.Unauthorized())
        val result = userRepository.createProfile(input)
        if (result is Result.Success) {
            sessionPreferences.setPendingRole(null)
        }
        return result
    }
}

class ResolveStartDestinationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sessionPreferences: SessionPreferencesRepository,
) {
    sealed interface StartDestination {
        data object Onboarding : StartDestination
        data object Start : StartDestination
        data object Login : StartDestination
        data object CreateProfile : StartDestination
        data object Home : StartDestination
    }

    suspend operator fun invoke(): StartDestination {
        if (!sessionPreferences.onboardingCompleted.first()) {
            return StartDestination.Onboarding
        }

        val user = authRepository.currentUser()
            ?: return StartDestination.Start

        return when (val profile = userRepository.getProfile(user.uid)) {
            is Result.Success -> {
                val data = profile.data

                if (data != null && data.profileCompleted) {
                    StartDestination.Home
                } else {
                    StartDestination.CreateProfile
                }
            }

            is Result.Error -> StartDestination.CreateProfile
            Result.Loading -> StartDestination.CreateProfile
        }
    }
}

class SetOnboardingCompletedUseCase @Inject constructor(
    private val sessionPreferences: SessionPreferencesRepository,
) {
    suspend operator fun invoke() = sessionPreferences.setOnboardingCompleted(true)
}

class SetPendingRoleUseCase @Inject constructor(
    private val sessionPreferences: SessionPreferencesRepository,
) {
    suspend operator fun invoke(role: UserRole) = sessionPreferences.setPendingRole(role)
}

class GetPendingRoleUseCase @Inject constructor(
    private val sessionPreferences: SessionPreferencesRepository,
) {
    suspend operator fun invoke(): UserRole? = sessionPreferences.pendingRole.first()
}
