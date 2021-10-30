package com.bykenyodarz.mskotlin.productos.controllers

import com.bykenyodarz.mskotlin.productos.services.IProductService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

@RestController
class ProductController(service: IProductService, env: Environment) {

    private val service: IProductService
    private val env: Environment

    @Value("\${server.port}")
    private val port: Int? = null

    init {
        service.also { this.service = it }
        env.also { this.env = it }
    }

    @GetMapping("/all")
    fun listAll(): ResponseEntity<*> {
        return ResponseEntity.ok().body(service.findAll().stream().map { producto ->
            producto.port = env.getProperty("local.server.port")!!.toInt()
            producto
        }.collect(Collectors.toList()))
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<*> {
        when (id) {
            "error" -> {
                throw IllegalArgumentException("Producto no Encontrado")
            }
            "espere" -> {
                TimeUnit.SECONDS.sleep(5L)

            }
            else -> return when (val product = service.findById(id)) {
                null -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con id: $id no se encuentra en la base de datos")
                else -> ResponseEntity.ok().body(product)
            }
        }
        return ResponseEntity.ok().body("Producto encontrado tarde")
    }
}