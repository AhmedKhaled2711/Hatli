package com.app.hatli.core.data.auth

import com.app.hatli.core.common.error.AppError
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException

object FirebaseErrorMapper {
    fun map(throwable: Throwable): AppError {
        if (throwable is CancellationException) throw throwable
        if (throwable is AppError) return throwable

        return when (throwable) {
            is UnknownHostException,
            is FirebaseNetworkException,
            -> AppError.NoInternet(throwable)

            is SocketTimeoutException -> AppError.Timeout(throwable)

            is FirebaseAuthInvalidCredentialsException -> {
                val code = throwable.errorCode
                when {
                    code.contains("invalid-verification-code", ignoreCase = true) ->
                        AppError.InvalidOtp(throwable)
                    code.contains("session-expired", ignoreCase = true) ->
                        AppError.OtpExpired(throwable)
                    else -> AppError.InvalidOtp(throwable)
                }
            }

            is FirebaseTooManyRequestsException ->
                AppError.FirebaseFailure("Too many attempts. Please wait and try again.", throwable)

            is FirebaseAuthException -> {
                when (throwable.errorCode) {
                    "ERROR_SESSION_EXPIRED",
                    "ERROR_INVALID_VERIFICATION_CODE",
                    -> AppError.InvalidOtp(throwable)
                    else -> AppError.FirebaseFailure(
                        message = throwable.localizedMessage ?: "Authentication failed.",
                        cause = throwable,
                    )
                }
            }

            else -> AppError.Unknown(
                message = throwable.localizedMessage ?: "Unexpected error.",
                cause = throwable,
            )
        }
    }
}
