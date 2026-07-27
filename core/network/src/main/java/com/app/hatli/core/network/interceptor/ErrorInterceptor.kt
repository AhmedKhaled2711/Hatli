package com.app.hatli.core.network.interceptor

import com.app.hatli.core.network.error.NetworkException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            val response = chain.proceed(chain.request())
            if (!response.isSuccessful) {
                throw NetworkException.HttpError(
                    code = response.code,
                    message = response.message.ifBlank { "Request failed" },
                )
            }
            response
        } catch (e: NetworkException) {
            throw e
        } catch (e: UnknownHostException) {
            throw NetworkException.NoConnectivity(e)
        } catch (e: SocketTimeoutException) {
            throw NetworkException.HttpError(408, "Timeout")
        } catch (e: IOException) {
            throw NetworkException.Unknown(e)
        }
    }
}
