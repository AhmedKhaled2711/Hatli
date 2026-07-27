package com.app.hatli.core.common.result

import com.app.hatli.core.common.error.AppError

fun Result.Error.toAppError(): AppError = when (val ex = exception) {
    is AppError -> ex
    else -> AppError.Unknown(message = message ?: "Unexpected error.", cause = ex)
}

fun Result.Error.userMessage(): String = toAppError().message
