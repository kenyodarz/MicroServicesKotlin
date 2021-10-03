package com.bykenyodarz.mskotlin.items.client

import com.bykenyodarz.mskotlin.items.models.Product
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "servicio-productos")
interface ProductoClienteRest {

    @GetMapping("/all")
    fun list(): List<Product>

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Product
}