package com.app.hatli.core.network.error

import java.io.IOException

sealed class NetworkException(
    message: String,
    cause: Throwable? = null,
) : IOException(message, cause) {
    class NoConnectivity(cause: Throwable? = null) :
        NetworkException("No internet connection", cause)

    class HttpError(val code: Int, message: String) :
        NetworkException("HTTP $code: $message")

    class Serialization(cause: Throwable) :
        NetworkException("Failed to parse response", cause)

    class Unknown(cause: Throwable? = null) :
        NetworkException(cause?.message ?: "Unknown network error", cause)
}
