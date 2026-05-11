package realtime_gateway.infrastructure.messaging.kafka.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import java.util.*

/**
   IMPORTANT!
   This is used to ensure that every instance (node) of this Realtime-Gateway would have a unique group id.
   This would cause all messages sent to the Realtime-Gateway to reach all of its instances and not 1.
 ***/

@Configuration
class KafkaBroadcastConsumerConfig(
    private val consumerFactory: ConsumerFactory<String, String>,
    @Value("\${spring.kafka.consumer.group-id}") private val baseGroupId: String
) {

    companion object {
        const val BROADCAST_KAFKA_LISTENER_CONTAINER_FACTORY = "broadcastKafkaListenerContainerFactory"
    }

    @Bean
    fun broadcastKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()

        val instanceId = System.getenv("HOSTNAME") ?: UUID.randomUUID().toString()
        val uniqueGroupId = "$baseGroupId-$instanceId"

        val props = HashMap(consumerFactory.configurationProperties)
        props[ConsumerConfig.GROUP_ID_CONFIG] = uniqueGroupId

        factory.consumerFactory = DefaultKafkaConsumerFactory(props)

        return factory
    }
}