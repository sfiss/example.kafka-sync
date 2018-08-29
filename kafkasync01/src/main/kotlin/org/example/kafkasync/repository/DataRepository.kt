package org.example.kafkasync.repository

import org.example.kafkasync.data.DataD
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface DataRepository : JpaRepository<DataD, Long>