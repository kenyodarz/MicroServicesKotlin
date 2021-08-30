package com.bykenyodarz.mskotlin.productos.controllers

import com.bykenyodarz.mskotlin.productos.services.IProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(service: IProductService) {

    private val service: IProductService

    init {
        service.also { this.service = it }
    }

    @GetMapping("/all")
    fun listAll(): ResponseEntity<*> {
        return ResponseEntity.ok().body(service.findAll())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<*>{
        return when(val product = service.findById(id)) {
            null -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Producto con id: $id no se encuentra en la base de datos")
            else -> ResponseEntity.ok().body(product)
        }
    }
}