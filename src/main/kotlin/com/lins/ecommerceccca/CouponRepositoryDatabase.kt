package com.lins.ecommerceccca

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinMapperFactory
import org.springframework.stereotype.Component

@Component
class CouponRepositoryDatabase {

    fun getCoupon (code: String) : Coupon{
        val jdbiHandle = Jdbi.create("jdbc:postgresql://localhost:5432/postgres", "root", "root").open()
        jdbiHandle.registerRowMapper(KotlinMapperFactory())
        val coupon = jdbiHandle.createQuery("select * from coupon where code = :code")
            .bind("code", code)
            .mapTo(Coupon::class.java)
            .findFirst()
            .get()
        jdbiHandle.close()
        return coupon
    }
}