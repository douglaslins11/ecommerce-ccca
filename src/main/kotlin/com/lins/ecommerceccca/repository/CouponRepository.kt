package com.lins.ecommerceccca.repository

import com.lins.ecommerceccca.model.Coupon
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<Coupon, String>