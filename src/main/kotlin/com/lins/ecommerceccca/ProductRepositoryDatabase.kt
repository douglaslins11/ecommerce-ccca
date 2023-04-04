package com.lins.ecommerceccca

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinMapperFactory
import org.springframework.stereotype.Component

@Component
class ProductRepositoryDatabase {

    fun getProduct (idProduct: Long) : Product{
        val jdbiHandle = Jdbi.create("jdbc:postgresql://localhost:5432/postgres", "root", "root").open()
        jdbiHandle.registerRowMapper(KotlinMapperFactory())
        val product = jdbiHandle.createQuery("select * from product where id = :id")
            .bind("id", idProduct)
            .mapTo(Product::class.java)
            .findFirst()
            .get()
        jdbiHandle.close()
        return product
    }
}