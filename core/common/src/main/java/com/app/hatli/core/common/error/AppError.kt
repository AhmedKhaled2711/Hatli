package com.app.hatli.core.common.error

/**
 * Domain-level errors. UI never sees Firebase/SDK exception types.
 */
sealed class AppError(
    override val message: String,
    override val cause: Throwable? = null,
) : Exception(message, cause) {
    class NoInternet(cause: Throwable? = null) :
        AppError("No internet connection. Check your network and try again.", cause)

    class Timeout(cause: Throwable? = null) :
        AppError("The request timed out. Please try again.", cause)

    class InvalidOtp(cause: Throwable? = null) :
        AppError("Invalid verification code. Please check and try again.", cause)

    class OtpExpired(cause: Throwable? = null) :
        AppError("This code has expired. Request a new one.", cause)

    class Cancelled(cause: Throwable? = null) :
        AppError("Operation cancelled.", cause)

    class InvalidInput(message: String) :
        AppError(message)

    class NotFound(message: String = "Not found.") :
        AppError(message)

    class Unauthorized(message: String = "Please sign in again.") :
        AppError(message)

    class FirebaseFailure(
        message: String = "Something went wrong. Please try again.",
        cause: Throwable? = null,
    ) : AppError(message, cause)

    class Unknown(
        message: String = "Unexpected error. Please try again.",
        cause: Throwable? = null,
    ) : AppError(message, cause)
}
