package org.example.kafkasync.data

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "data_d")
data class DataD(@Id private val id: Int, val content: String)