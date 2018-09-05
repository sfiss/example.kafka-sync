package org.example.kafkasync.services

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.Consumed
import org.example.kafkasync.data.*
import org.example.kafkasync.repository.DataRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.io.FileReader
import java.util.*
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.support.MessageBuilder


@Service
internal class DataService {
    @Autowired
    private lateinit var repo: DataRepository

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<Int, DataS>

    fun store(): String {
        val randomNumber = Math.random()
        var entity = DataD(content = "Data-D-$randomNumber")
        entity = repo.save(entity)
        val data = DataS(entity.id ?: 0, "Data-S-$randomNumber")
        notifyStream(data)
        return "Stored ${data.content}"
    }

    fun notifyStream(data: DataS) {
        val message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, DataSKafkaConfig.topic)
                .build()

        kafkaTemplate.send(message)
    }

    fun enhanceData(data: Data): DataE = DataE(data.content)

    @KafkaListener(topics = [DataSKafkaConfig.topic], groupId = DataSKafkaConfig.topic, containerFactory = "${DataSKafkaConfig.topic}ContainerFactory")
    fun logDataS(@Payload data: DataS) {
        println("Received S=$data")
    }

    @KafkaListener(topics = [DataDKafkaConfig.topic], groupId = DataDKafkaConfig.topic, containerFactory = "${DataDKafkaConfig.topic}ContainerFactory")
    fun logDataD(@Payload data: DataD) {
        println("Received D=$data")
    }

    @KafkaListener(topics = [DataEKafkaConfig.topic], groupId = DataEKafkaConfig.topic, containerFactory = "${DataEKafkaConfig.topic}ContainerFactory")
    fun logDataE(@Payload data: DataE) {
        println("Received E=$data")
    }

}