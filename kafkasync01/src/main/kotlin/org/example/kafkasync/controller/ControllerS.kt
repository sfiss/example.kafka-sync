package org.example.kafkasync.controller

import org.example.kafkasync.data.DataD
import org.example.kafkasync.repository.DataRepository
import org.example.kafkasync.services.DataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ControllerS {
    @Autowired
    private lateinit var repo: DataRepository

    @GetMapping("/test")
    fun test() =
            "Test successful"

    @GetMapping("/store")
    fun store(): String {
        val entity = DataD(0, "My Entity " + Math.random())
        repo.save(entity)
        return "Done"
    }
}