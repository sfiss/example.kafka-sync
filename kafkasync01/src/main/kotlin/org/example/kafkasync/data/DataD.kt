package org.example.kafkasync.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.IntegerDeserializer
import org.apache.kafka.common.serialization.IntegerSerializer
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.Consumed
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerde
import org.springframework.kafka.support.serializer.JsonSerializer
import java.util.HashMap
import javax.persistence.*

@Entity
@Table(name = "data_d")
data class DataD(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int? = null,
        override val content: String) : Data

@Configuration
class DataDKafkaConfig {

    companion object {
        const val topic: String = "data-d"

        fun consumed(): Consumed<Int, DataD> = Consumed.with(Serdes.IntegerSerde(),
                JsonSerde(DataD::class.java, ObjectMapper().registerModule(KotlinModule())))
    }

    @Value("\${spring.kafka.bootstrap-servers}")
    internal lateinit var bootstrapServers: String

    fun consumerConfigs(): Map<String, Any> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ConsumerConfig.GROUP_ID_CONFIG] = topic
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*" // trust all
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = IntegerDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        return props
    }

    fun consumerFactory(): ConsumerFactory<Int, DataD> {
        return DefaultKafkaConsumerFactory(consumerConfigs(),
                IntegerDeserializer(),
                JsonDeserializer(DataD::class.java, ObjectMapper().registerModule(KotlinModule())))
    }

    @Bean("${topic}ContainerFactory")
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<Int, DataD> {
        val factory = ConcurrentKafkaListenerContainerFactory<Int, DataD>()
        factory.consumerFactory = consumerFactory()
        return factory
    }
}