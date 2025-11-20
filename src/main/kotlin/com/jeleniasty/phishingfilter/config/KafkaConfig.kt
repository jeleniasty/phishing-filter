package com.jeleniasty.phishingfilter.config

import com.jeleniasty.phishingfilter.modules.processing.model.MessageInDto
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.UUIDDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import java.util.UUID

@EnableKafka
@Configuration
class KafkaConfig(
    @Value("\${spring.kafka.bootstrap-servers}") private val bootstrapServers: String,
    @Value("\${spring.kafka.consumer.group-id}") private val groupId: String,
    @Value("\${spring.kafka.consumer.auto-offset-reset}") private val autoOffsetReset: String,
    @Value("\${kafka.topic.phishing-message-log.name}") private val phishingTopic: String,
    @Value("\${kafka.topic.phishing-message-log.partitions}") private val phishingTopicPartitions: Int,
    @Value("\${kafka.topic.phishing-message-log.replication-factor}") private val phishingTopicReplicationFactor: Short
) {

    @Bean
    fun producerFactory(): ProducerFactory<UUID, MessageInDto> {
        val props = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to org.apache.kafka.common.serialization.UUIDSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
        )
        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<UUID, MessageInDto> = KafkaTemplate(producerFactory())

    @Bean
    fun consumerFactory(): ConsumerFactory<UUID, MessageInDto> {
        val valueDeserializer = JsonDeserializer(MessageInDto::class.java).apply {
            setRemoveTypeHeaders(false)
            addTrustedPackages("*")
        }

        val props = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG to groupId,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to autoOffsetReset,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to UUIDDeserializer::class.java,
        )

        return DefaultKafkaConsumerFactory(props, UUIDDeserializer(), valueDeserializer)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<UUID, MessageInDto> {
        val factory = ConcurrentKafkaListenerContainerFactory<UUID, MessageInDto>()
        factory.consumerFactory = consumerFactory()
        factory.setConcurrency(phishingTopicPartitions)
        return factory
    }

    @Bean
    fun phishingTopic(): NewTopic = NewTopic(phishingTopic, phishingTopicPartitions, phishingTopicReplicationFactor)
}
