package com.lins.ecommerceccca.service

object CpfAlgorithm  {

    private fun clean (cpf: String) = cpf.replace(Regex("[.-]"), "")

    private fun isValidLenght (cpf: String) = cpf.length == 11

    private fun allDigitsTheSame (cpf: String) = cpf.toCharArray().all { it == cpf[0] }

    private fun calculateDigit (cpf: String, factorParam: Int) : Int {
        var total = 0
        var factor = factorParam
        for (digit in cpf){
            if (factor > 1) total += digit.toString().toInt() * factor--
        }
        val rest = total%11
        return if(rest < 2) 0 else 11 - rest
    }

    private fun extractCheckDigit (cpf: String) = cpf.substring(cpf.length - 2, cpf.length)

    fun isValid(cpfParam: String): Boolean {
        if (cpfParam.isNullOrEmpty()) return false
        val cpf = clean(cpfParam)
        if (!isValidLenght(cpf)) return false
        if (allDigitsTheSame(cpf)) return false
        val digit1 = calculateDigit(cpf, 10)
        val digit2 = calculateDigit(cpf, 11)
        val calculateDigit = "$digit1$digit2"
        val actualDigit = extractCheckDigit(cpf)
        return actualDigit == calculateDigit
    }
}