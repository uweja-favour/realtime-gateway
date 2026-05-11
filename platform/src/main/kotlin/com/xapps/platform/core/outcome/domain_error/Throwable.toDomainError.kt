package com.xapps.platform.core.outcome.domain_error

import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.util.network.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException

fun Throwable.toDomainError(): DomainError =
    when (this) {

        // ✅ Network/Socket connectivity errors (Ktor multiplatform)
        is UnresolvedAddressException -> NetworkFailure.UnknownHost
        is ConnectTimeoutException -> NetworkFailure.Timeout
        is HttpRequestTimeoutException -> NetworkFailure.Timeout
        is SocketTimeoutException -> NetworkFailure.Timeout
        is IOException -> NetworkFailure.UnknownNetworkError(message = this.message, exception = this)
        is EOFException -> NetworkFailure.ParsingError(
            exception = this,
            message = "Unexpected end of response"
        )

        // ✅ Ktor HTTP response exceptions
        is RedirectResponseException -> NetworkFailure.HttpFailure(
            code = response.status.value,
            message = response.status.description,
            exception = this
        )
        is ClientRequestException -> {
            val code = response.status.value
            when (code) {
                429 -> NetworkFailure.RateLimit(
                    retryAfterSeconds = response.headers["Retry-After"]?.toIntOrNull()
                )
                in 400..499 -> NetworkFailure.ClientError(code, message)
                else -> NetworkFailure.HttpFailure(code, message, this)
            }
        }
        is ServerResponseException -> {
            val code = response.status.value
            NetworkFailure.ServerError(code, message)
        }
        is ResponseException -> {
            val code = response.status.value
            NetworkFailure.HttpFailure(code, message ?: "HTTP error", this)
        }

        // ✅ Serialization failures (Ktor/KotlinX)
        is SerializationException ->
            NetworkFailure.ParsingError(
                message = this.message ?: "Failed to parse server response",
                exception = this
            )

        // ✅ Job cancellation / coroutine cancellation
        is CancellationException ->
            GenericError(
                message = this.message ?: "Request was cancelled",
                exception = this
            )

        // ✅ SSE (Server Sent Events) plugin errors (if using Ktor SSE)
//        is SSEException ->
//            NetworkFailure.UnknownNetworkError(
//                message = this.message,
//                exception = this
//            )

        // ✅ Catch-all fallback
        else -> GenericError(
            message = this.message ?: "An unknown network error occurred",
            exception = this
        )
    }
