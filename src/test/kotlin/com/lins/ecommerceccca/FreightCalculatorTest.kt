package com.lins.ecommerceccca

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FreightCalculatorTest {

    @Test
    fun `Deve calcular o frete de um produtor`(){
        val product = Product(6L, "A", 1000.0, 100.0, 30.0, 10.0, 3.0, "BRL")
        val freight = FreightCalculator.calculate(product)
        Assertions.assertEquals(freight, 30.0)
    }
}