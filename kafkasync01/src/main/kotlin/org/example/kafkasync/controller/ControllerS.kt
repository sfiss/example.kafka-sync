package org.example.kafkasync.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ControllerS {
    @GetMapping("/test")
    fun test() =
            "Test successful"
}