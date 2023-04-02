package com.lins.ecommerceccca

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
data class Coupon(
    @Id
    val code: String,
    val percentage: Float,
    val expireDate: LocalDateTime
)
