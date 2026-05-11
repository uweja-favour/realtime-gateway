package com.xapps.platform.core.outcome.domain_error
// ---------------------------
// DomainError (single sealed hierarchy)
// ---------------------------
sealed interface DomainError {
    val message: String?
    val exception: Throwable
}

// Serialization / parsing errors
data class SerializationError(override val exception: Throwable) : DomainError {
    override val message: String? = exception.message
}

// Generic fallback domain error
data class GenericError(
    override val exception: Throwable,
    override val message: String? = exception.message
) : DomainError