package org.example.kafkasync

import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.example.kafkasync.data.DataDKafkaConfig
import org.example.kafkasync.data.DataEKafkaConfig
import org.example.kafkasync.data.DataSKafkaConfig
import org.example.kafkasync.services.DataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.stereotype.Service
import java.util.*

@SpringBootApplication
class SourceApplication

@Service
class Streaming {
    @Autowired
    @Qualifier("streamsConfig")
    private lateinit var streamsConfig: Properties

    @Autowired
    private lateinit var dataService: DataService


    fun stream(): KafkaStreams {
        val builder = StreamsBuilder()
        val dataSStream = builder.stream(DataSKafkaConfig.topic, DataSKafkaConfig.consumed()).mapValues { _, data -> dataService.enhanceData(data) }
        val dataDStream = builder.stream(DataDKafkaConfig.topic, DataDKafkaConfig.consumed()).mapValues { _, data -> dataService.enhanceData(data) }
        val dataEStream = dataSStream.merge(dataDStream).to(DataEKafkaConfig.topic, DataEKafkaConfig.produced())
        val topology = builder.build()
        return KafkaStreams(topology, streamsConfig)
    }
}

fun main(args: Array<String>) {
    val context = SpringApplication.run(SourceApplication::class.java, *args)

    val streams = context.beanFactory.getBean(Streaming::class.java).stream()
    streams.start()

    Runtime.getRuntime().addShutdownHook(Thread {
        streams.close()
    })
}

