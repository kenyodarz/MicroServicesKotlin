package com.bykenyodarz.mskotlin.items.services.impl

import com.bykenyodarz.mskotlin.items.client.ProductoClienteRest
import com.bykenyodarz.mskotlin.items.models.Item
import com.bykenyodarz.mskotlin.items.services.ItemService
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
@Primary
class ItemServiceFeign(productoClienteRest: ProductoClienteRest) : ItemService {

    private val productoClienteRest: ProductoClienteRest

    init {
        productoClienteRest.also { this.productoClienteRest = it }
    }

    override fun findAll(): List<Item> {
        return productoClienteRest.list()
            .stream()
            .map { Item(it, 1) }
            .collect(Collectors.toList())
    }

    override fun findById(id: String, quantity: Int): Item {
        val product = productoClienteRest.findById(id)
        return Item(product, quantity)
    }
}