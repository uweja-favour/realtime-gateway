package realtime_gateway.infrastructure.messaging.kafka.producer

import com.xapps.messaging.kafka.KafkaTopics
import com.xapps.messaging.kafka.events.UserOfflineEvent
import com.xapps.messaging.kafka.events.UserOnlineEvent
import com.xapps.platform.core.compression.ObjectCompressionService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaUserPresenceEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
    private val compressionService: ObjectCompressionService
) {

    fun publishUserOnline(event: UserOnlineEvent) {
        val compressed: ByteArray = compressionService.compress(
            UserOnlineEvent.serializer(),
            event
        )

        kafkaTemplate.send(
            KafkaTopics.User.ONLINE,
            event.userId,
            compressed
        )
    }

    fun publishUserOffline(event: UserOfflineEvent) {
        val compressed: ByteArray = compressionService.compress(
            UserOfflineEvent.serializer(),
            event
        )

        kafkaTemplate.send(
            KafkaTopics.User.OFFLINE,
            event.userId,
            compressed
        )
    }
}