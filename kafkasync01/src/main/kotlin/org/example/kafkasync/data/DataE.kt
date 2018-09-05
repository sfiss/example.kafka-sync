package org.example.kafkasync.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.IntegerDeserializer
import org.apache.kafka.common.serialization.IntegerSerializer
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Produced
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerde
import org.springframework.kafka.support.serializer.JsonSerializer
import java.util.*
import javax.persistence.*

data class DataE(
        override val content: String) : Data

@Configuration
class DataEKafkaConfig {

    companion object {
        const val topic: String = "data-e"

        fun produced(): Produced<Int, DataE> = Produced.with(Serdes.IntegerSerde(),
                JsonSerde(DataE::class.java, ObjectMapper().registerModule(KotlinModule())))
    }

    @Value("\${spring.kafka.bootstrap-servers}")
    internal lateinit var bootstrapServers: String

    @Value("\${spring.kafka.stream-app.id}")
    internal lateinit var streamAppId: String

    @Bean("streamsConfig")
    fun streamsConfig(): Properties {
        val props = Properties()
        props[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[StreamsConfig.APPLICATION_ID_CONFIG] = streamAppId
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*" // trust all
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

    fun consumerFactory(): ConsumerFactory<Int, DataE> {
        return DefaultKafkaConsumerFactory(consumerConfigs(),
                IntegerDeserializer(),
                JsonDeserializer(DataE::class.java, ObjectMapper().registerModule(KotlinModule())))
    }

    @Bean("${topic}ContainerFactory")
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<Int, DataE>  {
        val factory = ConcurrentKafkaListenerContainerFactory<Int, DataE> ()
        factory.consumerFactory = consumerFactory()
        return factory
    }
}