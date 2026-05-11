package com.xapps.platform.core.outcome.domain_error

sealed class NetworkFailure(
    override val message: String? = null,
    override val exception: Throwable = Throwable("Unknown exception")
) : DomainError {

    object NoConnection : NetworkFailure("No internet connection")
    object Timeout : NetworkFailure("Request timed out")
    data class HttpFailure(
        val code: Int,
        override val message: String? = null,
        override val exception: Throwable
    ) : NetworkFailure(message, exception)

    object EmptyBody : NetworkFailure("Response has an empty body")
    data class NoData(override val message: String?) : NetworkFailure(message)
    data class ParsingError(
        override val message: String? = null,
        override val exception: Throwable
    ) : NetworkFailure(message, exception)

    // Additional network-specific failures
    object UnknownHost : NetworkFailure("Cannot resolve host. Check your internet connection.")
    object SSLException : NetworkFailure("SSL certificate error. Connection not secure.")
    object ConnectionRefused : NetworkFailure("Connection refused by server.")
    object ConnectionReset : NetworkFailure("Connection was reset unexpectedly.")
    object RedirectLimitExceeded : NetworkFailure("Too many redirects.")
    data class RateLimit(
        val retryAfterSeconds: Int? = null,
        override val message: String? = "Rate limit exceeded. Please try again later."
    ) : NetworkFailure(message)
    data class ServerError(
        val code: Int,
        override val message: String? = null
    ) : NetworkFailure(message)
    data class ClientError(
        val code: Int,
        override val message: String? = null
    ) : NetworkFailure(message)
    data class UnknownNetworkError(
        override val message: String? = "An unknown network error occurred",
        override val exception: Throwable
    ) : NetworkFailure(message, exception)
}

fun NetworkFailure.userFriendlyMessage(): String =
    when (this) {
        is NetworkFailure.NoConnection, is NetworkFailure.UnknownHost ->
            "No internet connection. Please check your Wi-Fi or mobile data and try again."
        is NetworkFailure.Timeout ->
            "The request took too long. Check your internet connection and try again."
        is NetworkFailure.RateLimit ->
            "Too many attempts in a short time. Please wait a moment and try again."
        is NetworkFailure.ClientError -> when (code) {
            401 -> "Invalid email or password. Please try again."
            403 -> "You don't have permission to perform this action."
            400 -> "Invalid request. Please check your input and try again."
            404 -> "We couldn’t find what you were looking for. Please try again."
            409 -> "This account or request is already in use. Please check and try again."
            else -> "Request error $code. Please check your input and try again."
        }
        is NetworkFailure.ServerError ->
            "The server is temporarily unavailable. Please try again later."
        is NetworkFailure.ParsingError ->
            "We couldn't process the server response. Please try again."
        is NetworkFailure.SSLException ->
            "A secure connection could not be established. Please check your network."
        is NetworkFailure.ConnectionReset ->
            "The connection was lost unexpectedly. Please try again."
        is NetworkFailure.EmptyBody ->
            "The server returned no data. Please try again."
        is NetworkFailure.RedirectLimitExceeded ->
            "Too many redirects. Cannot complete the request."
        is NetworkFailure.HttpFailure ->
            "HTTP error ${this.code}: ${this.message ?: "An unexpected error occurred."}"
        is NetworkFailure.UnknownNetworkError ->
            "Something went wrong. Please try again later."
        else ->
            "Something went wrong. Please try again later."
    }
