package com.bykenyodarz.mskotlin.items.controllers

import com.bykenyodarz.mskotlin.items.models.Item
import com.bykenyodarz.mskotlin.items.models.Product
import com.bykenyodarz.mskotlin.items.services.ItemService
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI


@RefreshScope
@EnableAutoConfiguration
@RestController
class ItemsControllers(itemService: ItemService, env: Environment) {

    private val logger: Logger = LoggerFactory.getLogger(ItemsControllers::class.java)

    private val itemService: ItemService
    private val env: Environment

    @Value("\${configuracion.texto}")
    private val text: String? = null


    init {
        itemService.also { this.itemService = it }
        env.also { this.env = it }
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

    @GetMapping("/obtener-config")
    fun getConfig(@Value("\${server.port}") puerto: String): ResponseEntity<*>? {
        logger.info("Configuración -> {}", text)
        val jsonResponse: MutableMap<String, String> = HashMap()
        jsonResponse["text"] = text!!
        jsonResponse["puerto"] = puerto
        if (env.activeProfiles.isNotEmpty() && env.activeProfiles[0].equals("dev")) {
            jsonResponse["autor.nombre"] = env.getProperty("configuracion.autor.nombre",
                "Propiedad [autor.nombre] no encontrada")
            jsonResponse["autor.email"] = env.getProperty("configuracion.autor.email",
                "Propiedad [autor.nombre] no encontrada")
        }
        return ResponseEntity(jsonResponse, HttpStatus.OK)
    }

    @PostMapping("/save")
    fun crear(@RequestBody product: Product): ResponseEntity<*>? {
        val productResponse = itemService.save(product)
        return ResponseEntity.created(URI.create("/" + productResponse.id)
        ).body<Any>(productResponse)
    }

    @DeleteMapping("/eliminar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun eliminar(@PathVariable id: String) {
        itemService.delete(id)
    }

}