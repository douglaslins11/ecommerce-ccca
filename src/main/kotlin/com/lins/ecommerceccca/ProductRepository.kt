package com.lins.ecommerceccca

interface ProductRepository {
    fun getProduct (idProduct: Long) : Product
}
