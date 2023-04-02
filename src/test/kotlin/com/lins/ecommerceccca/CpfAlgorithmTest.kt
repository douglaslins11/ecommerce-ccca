package com.lins.ecommerceccca

import com.lins.ecommerceccca.CpfAlgorithm
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class CpfAlgorithmTest {

    @ParameterizedTest
    @ValueSource(strings = [
        "684.053.160-00",
        "746.971.314-01",
        "083.772.520-82"
    ])
    fun `Deve retornar True validar um CPF`(cpf: String){
        Assertions.assertTrue(CpfAlgorithm.isValid(cpf))
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "054.399.0",
        "054.399.150-43455",
        "0543991504322"
    ])
    fun `Deve retornar False quando verificar um CPF inválido`(cpf: String){
        Assertions.assertFalse(CpfAlgorithm.isValid(cpf))
    }

    @ParameterizedTest
    @ValueSource(strings = ["111.111.111-11", "222.222.222-22"])
    fun `Deve testar um CPF inválido quando todos os dígitos forem iguais`(cpf: String){
        Assertions.assertFalse(CpfAlgorithm.isValid(cpf))
    }
}