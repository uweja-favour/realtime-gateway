package com.xapps.messaging.kafka.events

import kotlinx.serialization.Serializable

@Serializable
data class NoteSummaryDeliveredEvent(
    val userId: String,
    val noteSummaryIds: List<String>
)
