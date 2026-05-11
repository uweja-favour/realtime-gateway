package com.xapps.model

@JvmInline
value class DeliveryStatusCode(val value: String)

enum class DeliveryStatus(
    val code: DeliveryStatusCode,
    val displayName: String
) {
    PENDING(DeliveryStatusCode("pending"), "Pending"),
    DELIVERED(DeliveryStatusCode("delivered"), "Delivered"),
    UNKNOWN(DeliveryStatusCode("unknown"), "Unknown");

    companion object {
        private val byCodes = entries.associateBy { it.code }

        fun fromCode(code: DeliveryStatusCode): DeliveryStatus {
            return byCodes[code] ?: UNKNOWN
        }
    }
}