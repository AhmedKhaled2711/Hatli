package com.app.hatli.core.domain.usecase.auth

import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.repository.AuthRepository
import com.app.hatli.core.model.OtpSession
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(phoneNumber: String): Result<OtpSession> =
        authRepository.sendOtp(phoneNumber.trim())
}

class ResendOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(phoneNumber: String): Result<OtpSession> =
        authRepository.resendOtp(phoneNumber.trim())
}

class VerifyOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(verificationId: String, smsCode: String): Result<*> =
        authRepository.verifyOtp(verificationId, smsCode.trim())
}

class IsLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Boolean = authRepository.isLoggedIn()
}

class ObserveAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke() = authRepository.authState
}

class GetCurrentAuthUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke() = authRepository.currentUser()
}

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val clearLocalUserData: ClearLocalUserDataUseCase,
) {
    suspend operator fun invoke(): Result<Unit> {
        // Clear local first, then sign out. Callers must cancel Firestore observers before this.
        clearLocalUserData()
        return authRepository.logout()
    }
}

class ClearLocalUserDataUseCase @Inject constructor(
    private val userRepository: com.app.hatli.core.domain.repository.UserRepository,
    private val sessionPreferences: com.app.hatli.core.domain.repository.SessionPreferencesRepository,
) {
    suspend operator fun invoke() {
        userRepository.clearLocalCache()
        sessionPreferences.clearSessionPreferences()
    }
}
