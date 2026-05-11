package com.xapps.messaging.kafka.events

import kotlinx.serialization.Serializable

@Serializable
data class UserOnlineEvent(
    val userId: String
)


