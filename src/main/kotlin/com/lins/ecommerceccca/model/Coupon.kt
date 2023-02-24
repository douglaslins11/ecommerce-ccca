package com.lins.ecommerceccca.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Coupon(
    @Id
    val code: String,
    val percentage: Float
)
