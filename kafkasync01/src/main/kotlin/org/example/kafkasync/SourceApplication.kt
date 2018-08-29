package org.example.kafkasync

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SourceApplication

fun main(args: Array<String>) {
        SpringApplication.run(SourceApplication::class.java, *args)
}