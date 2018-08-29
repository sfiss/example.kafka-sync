package org.example.kafkasync.application

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("org.example.kafkasync")
class SourceApplication

fun main(args: Array<String>) {
        SpringApplication.run(SourceApplication::class.java, *args)
}