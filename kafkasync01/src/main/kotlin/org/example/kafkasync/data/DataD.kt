package org.example.kafkasync.data

import javax.persistence.*

@Entity
@Table(name = "data_d")
data class DataD(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private val id: Int? = null, val content: String)