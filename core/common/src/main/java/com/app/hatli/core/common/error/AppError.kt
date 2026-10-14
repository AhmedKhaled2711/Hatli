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

    fun toLocalizedMessage(isAr: Boolean = java.util.Locale.getDefault().language == "ar"): String = when (this) {
        is NoInternet -> if (isAr) "لا يوجد اتصال بالإنترنت. يرجى التحقق من الشبكة والمحاولة مرة أخرى." else "No internet connection. Check your network and try again."
        is Timeout -> if (isAr) "انتهت مهلة الطلب. يرجى المحاولة مرة أخرى." else "The request timed out. Please try again."
        is InvalidOtp -> if (isAr) "رمز التحقق غير صحيح. يرجى التأكد والمحاولة مرة أخرى." else "Invalid verification code. Please check and try again."
        is OtpExpired -> if (isAr) "انتهت صلاحية الرمز. يرجى طلب رمز جديد." else "This code has expired. Request a new one."
        is Cancelled -> if (isAr) "تم إلغاء العملية." else "Operation cancelled."
        is NotFound -> if (isAr) "العنصر غير موجود." else (message.ifBlank { "Not found." })
        is Unauthorized -> if (isAr) "يرجى تسجيل الدخول مرة أخرى." else (message.ifBlank { "Please sign in again." })
        is FirebaseFailure -> if (isAr) "حدث خطأ ما. يرجى المحاولة مرة أخرى." else (message.ifBlank { "Something went wrong. Please try again." })
        is Unknown -> if (isAr) "حدث خطأ غير متوقع. يرجى المحاولة مرة أخرى." else (message.ifBlank { "Unexpected error. Please try again." })
        is InvalidInput -> message
    }
}
