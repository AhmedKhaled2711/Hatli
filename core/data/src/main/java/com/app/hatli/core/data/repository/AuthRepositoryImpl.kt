package com.app.hatli.core.data.repository

import com.app.hatli.core.common.dispatcher.IoDispatcher
import com.app.hatli.core.common.error.AppError
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.util.PhoneSanitizer
import com.app.hatli.core.data.auth.FirebaseAuthDataSource
import com.app.hatli.core.data.auth.FirebaseErrorMapper
import com.app.hatli.core.domain.repository.AuthRepository
import com.app.hatli.core.model.AuthUser
import com.app.hatli.core.model.OtpSession
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AuthRepository {

    override val authState: Flow<AuthUser?> = authDataSource.authState

    override fun currentUser(): AuthUser? = authDataSource.currentUser()

    override fun isLoggedIn(): Boolean = authDataSource.isLoggedIn()

    override suspend fun sendOtp(phoneNumber: String): Result<OtpSession> =
        withContext(ioDispatcher) {
            runAuth {
                val sanitized = PhoneSanitizer.sanitizeEgyptianPhoneNumber(phoneNumber)
                if (!PhoneSanitizer.isValidEgyptianPhoneNumber(sanitized)) {
                    throw AppError.InvalidInput("Please enter a valid Egyptian mobile number (e.g. 01007394856).")
                }
                authDataSource.sendOtp(sanitized, resend = false)
            }
        }

    override suspend fun resendOtp(phoneNumber: String): Result<OtpSession> =
        withContext(ioDispatcher) {
            runAuth {
                val sanitized = PhoneSanitizer.sanitizeEgyptianPhoneNumber(phoneNumber)
                if (!PhoneSanitizer.isValidEgyptianPhoneNumber(sanitized)) {
                    throw AppError.InvalidInput("Please enter a valid Egyptian mobile number (e.g. 01007394856).")
                }
                authDataSource.sendOtp(sanitized, resend = true)
            }
        }

    override suspend fun verifyOtp(verificationId: String, smsCode: String): Result<AuthUser> =
        withContext(ioDispatcher) {
            runAuth {
                if (smsCode.length < 6) throw AppError.InvalidOtp()
                authDataSource.verifyOtp(verificationId, smsCode)
            }
        }

    override suspend fun logout(): Result<Unit> = withContext(ioDispatcher) {
        runAuth {
            authDataSource.logout()
        }
    }

    private inline fun <T> runAuth(block: () -> T): Result<T> = try {
        Result.Success(block())
    } catch (t: Throwable) {
        Result.Error(FirebaseErrorMapper.map(t))
    }
}
