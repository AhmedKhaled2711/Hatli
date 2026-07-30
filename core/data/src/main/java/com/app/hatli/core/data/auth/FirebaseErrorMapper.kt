package com.app.hatli.core.data.auth

import com.app.hatli.core.common.error.AppError
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.storage.StorageException
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

            is StorageException -> mapStorage(throwable)

            else -> {
                // StorageException is sometimes wrapped (e.g. ExecutionException / IOException).
                val storage = findCause<StorageException>(throwable)
                if (storage != null) mapStorage(storage)
                else AppError.Unknown(
                    message = throwable.localizedMessage ?: "Unexpected error.",
                    cause = throwable,
                )
            }
        }
    }

    private fun mapStorage(error: StorageException): AppError = when (error.errorCode) {
        StorageException.ERROR_OBJECT_NOT_FOUND ->
            AppError.FirebaseFailure(
                "Could not upload image (file missing on Storage). " +
                    "Confirm Firebase Storage is enabled and Security Rules allow authenticated writes " +
                    "to users/{userId}/**, then try again.",
                error,
            )
        StorageException.ERROR_NOT_AUTHORIZED,
        StorageException.ERROR_RETRY_LIMIT_EXCEEDED,
        ->
            AppError.FirebaseFailure(
                "You don't have permission to upload images. " +
                    "Update Storage Rules to allow authenticated users to write their own folder, " +
                    "then sign in again.",
                error,
            )
        StorageException.ERROR_QUOTA_EXCEEDED ->
            AppError.FirebaseFailure("Storage quota exceeded. Try again later.", error)
        else ->
            AppError.FirebaseFailure(
                error.localizedMessage ?: "Image upload failed. Please try again.",
                error,
            )
    }

    private inline fun <reified T : Throwable> findCause(throwable: Throwable): T? {
        var current: Throwable? = throwable
        while (current != null) {
            if (current is T) return current
            current = current.cause
        }
        return null
    }
}
