package realtime_gateway.infrastructure.messaging.kafka.config

import org.apache.kafka.common.TopicPartition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.util.backoff.FixedBackOff

@Configuration
class KafkaConfig {

    /**
     * KafkaTemplate bean is needed for DeadLetterPublishingRecoverer.
     */
    @Bean
    fun kafkaTemplate(
        producerFactory: ProducerFactory<String, ByteArray>
    ): KafkaTemplate<String, ByteArray> {
        return KafkaTemplate(producerFactory)
    }

    /**
     * Configures a Kafka listener container factory with:
     * - Manual acknowledgment
     * - Dead-letter publishing for failed messages
     * - Retry mechanism before sending to DLT
     */
    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, ByteArray>,
        kafkaTemplate: KafkaTemplate<String, ByteArray>
    ): ConcurrentKafkaListenerContainerFactory<String, ByteArray> {

        val factory = ConcurrentKafkaListenerContainerFactory<String, ByteArray>()
        factory.consumerFactory = consumerFactory

        val backOff = FixedBackOff(1000L, 3)

        val deadLetterRecoverer = DeadLetterPublishingRecoverer(kafkaTemplate) { record, _ ->
            TopicPartition("${record.topic()}.DLT", record.partition())
        }

        val errorHandler = DefaultErrorHandler(deadLetterRecoverer, backOff)
        factory.setCommonErrorHandler(errorHandler)

        factory.containerProperties.ackMode = ContainerProperties.AckMode.RECORD

        return factory
    }
}