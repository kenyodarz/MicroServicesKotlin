package com.bykenyodarz.mskotlin.items.controllers

import com.bykenyodarz.mskotlin.items.models.Item
import com.bykenyodarz.mskotlin.items.models.Product
import com.bykenyodarz.mskotlin.items.services.ItemService
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.web.bind.annotation.*


@RefreshScope
@EnableAutoConfiguration
@RestController
class ItemsControllers(itemService: ItemService) {

    private val logger: Logger = LoggerFactory.getLogger(ItemsControllers::class.java)

    private val itemService: ItemService


    init {
        itemService.also { this.itemService = it }
    }

    @GetMapping("/all")
    fun listar(
        @RequestParam(name = "nombre", required = false) nombre: String,
        @RequestHeader(name = "token-request", required = false) token: String
    ):
            List<Item> {
        logger.info("Param: {}", nombre)
        logger.info("Header: {}", token)
        return itemService.findAll()
    }

    @GetMapping("/{id}/{cantidad}")
    @CircuitBreaker(name = "PRODUCTOS", fallbackMethod = "alterMethod")
    fun getItem(@PathVariable id: String, @PathVariable cantidad: Int): Item {
        return itemService.findById(id, cantidad)
    }

    fun alterMethod(id: String, cantidad: Int, ex: Exception): Item? {
        val item = Item()
        val product = Product()
        with(item) {
            product.id = id
            product.name = "Producto Prueba"
            product.price = 500.00
            this.quantity = cantidad
            this.product = product
        }
        logger.info("Response 200, fallback method for error: {}", ex.message)
        return item
    }

}