package com.app.hatli.core.domain.repository

import com.app.hatli.core.common.result.Result
import com.app.hatli.core.model.AuthUser
import com.app.hatli.core.model.OtpSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<AuthUser?>
    fun currentUser(): AuthUser?
    fun isLoggedIn(): Boolean
    suspend fun sendOtp(phoneNumber: String): Result<OtpSession>
    suspend fun resendOtp(phoneNumber: String): Result<OtpSession>
    suspend fun verifyOtp(verificationId: String, smsCode: String): Result<AuthUser>
    suspend fun logout(): Result<Unit>
}
