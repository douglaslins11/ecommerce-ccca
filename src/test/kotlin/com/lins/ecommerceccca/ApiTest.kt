package com.lins.ecommerceccca

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiTest {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext
    private lateinit var mockMvc: MockMvc
    private val objectMapper = jacksonObjectMapper()

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun `Não deve criar um pedido quando o CPF for inválido`() {
        val input = CreateOrderInput(
            "099.957.300-44", mutableListOf(
                OrderDto(1L, 1),
                OrderDto(2L, 1),
                OrderDto(3L, 3)
            )
        )
        val response = mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(input))
        )
            .andExpect(status().isUnprocessableEntity)
            .andReturn()
        val output = objectMapper.readValue(response.response.contentAsString, CreateOrderOutput::class.java)
        Assertions.assertEquals("Invalid CPF", output.message)
    }

    @Test
    fun `Deve criar um pedido vazio com o CPF válido`() {
        val input = CreateOrderInput(
            "515.089.870-84", mutableListOf()
        )
        val response = mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andReturn()
        val output = objectMapper.readValue(response.response.contentAsString, CreateOrderOutput::class.java)
        Assertions.assertEquals(0.0, output.total)
    }

    @Test
    fun `Deve criar um pedido com 3 produtos`() {
        val items = mutableListOf(
            OrderDto(1L, 1),
            OrderDto(2L, 1),
            OrderDto(3L, 3)
        )
        val input = CreateOrderInput(
            "515.089.870-84",
            items
        )
        val response = mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andReturn()
        val output = objectMapper.readValue(response.response.contentAsString, CreateOrderOutput::class.java)
        Assertions.assertEquals(6090.0, output.total)
    }

    @Test
    fun `Deve criar um pedido com 3 produtos com cupom de desconto`() {
        val items = mutableListOf(
            OrderDto(1L, 1),
            OrderDto(2L, 1),
            OrderDto(3L, 3)
        )
        val input = CreateOrderInput(
            "515.089.870-84",
            items,
            "VALE20",
        )
        val response = mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andReturn()
        val output = objectMapper.readValue(response.response.contentAsString, CreateOrderOutput::class.java)
        Assertions.assertEquals(4872.0, output.total)
    }

    @Test
    fun `Não deve aplicar cupom de desconto expirado`() {
        val items = mutableListOf(
            OrderDto(1L, 1),
            OrderDto(2L, 1),
            OrderDto(3L, 3)
        )
        val input = CreateOrderInput(
            "515.089.870-84",
            items,
            "VALE10",
        )
        val response = mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andReturn()
        val output = objectMapper.readValue(response.response.contentAsString, CreateOrderOutput::class.java)
        Assertions.assertEquals(6090.0, output.total)
    }

    @Test
    fun `Não deve criar um pedido quando a quantidade de um item for negativa`() {
        val items = mutableListOf(
            OrderDto(1L, -1),
        )
        val input = CreateOrderInput(
            "515.089.870-84",
            items,
        )
        val response = mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(input))
        )
            .andExpect(status().isUnprocessableEntity)
            .andReturn()
        val output = objectMapper.readValue(response.response.contentAsString, CreateOrderOutput::class.java)
        Assertions.assertEquals("Invalid quantity", output.message)
    }

    @Test
    fun `Não deve criar um pedido quando o item estiver duplicado`() {
        val items = mutableListOf(
            OrderDto(1L, 1),
            OrderDto(1L, 1),
        )
        val input = CreateOrderInput(
            "515.089.870-84",
            items,
        )
        val response = mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(input))
        )
            .andExpect(status().isUnprocessableEntity)
            .andReturn()
        val output = objectMapper.readValue(response.response.contentAsString, CreateOrderOutput::class.java)
        Assertions.assertEquals("Duplicated item", output.message)
    }

    @Test
    fun `Deve criar um pedido com 1 produto calculando o frete`() {
        val items = mutableListOf(
            OrderDto(1L, 3),
        )
        val input = CreateOrderInput(
            cpf = "515.089.870-84",
            items = items,
            from = "22060030",
            to = "88015600"
        )
        val response = mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andReturn()
        val output = objectMapper.readValue(response.response.contentAsString, CreateOrderOutput::class.java)
        Assertions.assertEquals(3090.0, output.total)
        Assertions.assertEquals(90.0, output.freight)
    }

    @Test
    fun `Não deve criar um pedido se o produto tiver alguma dimensão negativa`() {
        val items = mutableListOf(
            OrderDto(4L, 1),
        )
        val input = CreateOrderInput(
            "515.089.870-84",
            items,
        )
        val response = mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(input))
        )
            .andExpect(status().isUnprocessableEntity)
            .andReturn()
        val output = objectMapper.readValue(response.response.contentAsString, CreateOrderOutput::class.java)
        Assertions.assertEquals("Invalid dimension", output.message)
    }

    @Test
    fun `Deve criar um pedido com 1 produto calculando o frete com valor mínimo`() {
        val items = mutableListOf(
            OrderDto(3L, 1),
        )
        val input = CreateOrderInput(
            cpf = "515.089.870-84",
            items = items,
            from = "22060030",
            to = "88015600"
        )
        val response = mockMvc.perform(
            post("/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andReturn()
        val output = objectMapper.readValue(response.response.contentAsString, CreateOrderOutput::class.java)
        Assertions.assertEquals(40.0, output.total)
        Assertions.assertEquals(10.0, output.freight)
    }
}