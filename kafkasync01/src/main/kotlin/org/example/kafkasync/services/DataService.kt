package org.example.kafkasync.services

import org.example.kafkasync.data.DataD
import org.example.kafkasync.data.DataS
import org.example.kafkasync.repository.DataRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
internal class DataService {
    @Autowired
    private lateinit var repo: DataRepository

    fun store(): String {
        val randomNumber = Math.random()
        val entity = DataD(content = "Data-D-$randomNumber")
        val data = DataS("Data-S-$randomNumber")
        repo.save(entity)
        return "Stored ${data.content}"
    }
}