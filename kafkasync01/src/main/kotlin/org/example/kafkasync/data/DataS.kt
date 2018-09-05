package org.example.kafkasync.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.IntegerDeserializer
import org.apache.kafka.common.serialization.IntegerSerializer
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.Consumed
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.*
import org.springframework.kafka.support.serializer.JsonSerializer
import java.util.HashMap
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerde


data class DataS(
        val id: Int,
        override val content: String) : Data

@Configuration
internal class DataSKafkaConfig {

    companion object {
        const val topic: String = "data-s"

        fun consumed(): Consumed<Int, DataS> = Consumed.with(Serdes.IntegerSerde(),
                JsonSerde(DataS::class.java, ObjectMapper().registerModule(KotlinModule())))
    }

    @Value("\${spring.kafka.bootstrap-servers}")
    internal lateinit var bootstrapServers: String

    fun producerConfigs(): Map<String, Any> {
        val props = HashMap<String, Any>()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = IntegerSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        return props
    }

    fun consumerConfigs(): Map<String, Any> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ConsumerConfig.GROUP_ID_CONFIG] = topic
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*" // trust all
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = IntegerDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        return props
    }

    fun producerFactory(): ProducerFactory<Int, DataS> {
        return DefaultKafkaProducerFactory(producerConfigs())
    }

    fun consumerFactory(): ConsumerFactory<Int, DataS> {
        return DefaultKafkaConsumerFactory(consumerConfigs(),
                IntegerDeserializer(),
                JsonDeserializer(DataS::class.java, ObjectMapper().registerModule(KotlinModule())))
    }

    @Bean("${topic}ContainerFactory")
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<Int, DataS>  {
        val factory = ConcurrentKafkaListenerContainerFactory<Int, DataS> ()
        factory.consumerFactory = consumerFactory()
        return factory
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<Int, DataS> {
        return KafkaTemplate(producerFactory())
    }
}