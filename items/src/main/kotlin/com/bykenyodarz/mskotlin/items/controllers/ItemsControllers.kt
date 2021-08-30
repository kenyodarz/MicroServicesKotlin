package com.bykenyodarz.mskotlin.items.controllers

import com.bykenyodarz.mskotlin.items.models.Item
import com.bykenyodarz.mskotlin.items.services.ItemService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class ItemsControllers(itemService: ItemService) {

    private val itemService: ItemService

    init {
        itemService.also { this.itemService = it }
    }

    @GetMapping("/all")
    fun listar(): List<Item> {
        return itemService.findAll()
    }

    @GetMapping("/{id}/{cantidad}")
    fun getItem(@PathVariable id: String, @PathVariable cantidad: Int): Item {
        return itemService.findById(id, cantidad)
    }

}