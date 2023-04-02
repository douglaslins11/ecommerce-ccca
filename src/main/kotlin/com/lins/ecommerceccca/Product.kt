package com.lins.ecommerceccca

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val description: String,
    val price: Double,
    val width: Double,
    val height: Double,
    val length: Double,
    val weight: Double
)
