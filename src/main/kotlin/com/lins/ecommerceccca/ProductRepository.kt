package com.lins.ecommerceccca

import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository <Product, Long>