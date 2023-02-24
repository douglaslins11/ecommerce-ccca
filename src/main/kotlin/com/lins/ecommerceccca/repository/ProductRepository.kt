package com.lins.ecommerceccca.repository

import com.lins.ecommerceccca.model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository <Product, Long>