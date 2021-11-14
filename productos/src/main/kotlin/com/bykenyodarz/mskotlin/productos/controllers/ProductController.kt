package com.bykenyodarz.mskotlin.productos.controllers

import com.bykenyodarz.mskotlin.productos.models.Product
import com.bykenyodarz.mskotlin.productos.services.IProductService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.validation.Valid

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

    @PostMapping("/save")
    fun save(@Valid @RequestBody entity: Product, result: BindingResult): ResponseEntity<*> {
        return if (result.hasErrors()) validar(result) else ResponseEntity.status(HttpStatus.CREATED)
            .body(service.save(entity))
    }

    // Validador de campos
    fun validar(result: BindingResult): ResponseEntity<*> {
        val errores: MutableMap<String, Any> = HashMap()
        result.fieldErrors.forEach(Consumer { err: FieldError ->
            errores[err.field] = " El campo " + err.field + " " + err.defaultMessage
        })
        return ResponseEntity.badRequest().body<Map<String, Any>>(errores)
    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<*> {
        val entity: Product? = service.findById(id)
        if (entity != null) {
            service.delete(entity)
        } else return ResponseEntity.notFound().build<Any>()
        return ResponseEntity<Any>(entity, HttpStatus.ACCEPTED)
    }
}