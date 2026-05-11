package com.xapps.messaging.kafka.events

import kotlinx.serialization.Serializable

@Serializable
data class UserOfflineEvent(
    val userId: String
)
