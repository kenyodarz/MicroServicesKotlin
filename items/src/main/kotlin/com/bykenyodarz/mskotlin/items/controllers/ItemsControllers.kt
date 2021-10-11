package com.bykenyodarz.mskotlin.items.controllers

import com.bykenyodarz.mskotlin.items.models.Item
import com.bykenyodarz.mskotlin.items.services.ItemService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*


@RestController
class ItemsControllers(itemService: ItemService) {

    private val logger: Logger = LoggerFactory.getLogger(ItemsControllers::class.java)

    private val itemService: ItemService

    init {
        itemService.also { this.itemService = it }
    }

    @GetMapping("/all")
    fun listar(@RequestParam(name = "nombre", required = false) nombre: String,
               @RequestHeader(name = "token-request", required = false) token: String):
            List<Item> {
        logger.info("Param: {}", nombre)
        logger.info("Header: {}", token)
        return itemService.findAll()
    }

    @GetMapping("/{id}/{cantidad}")
    fun getItem(@PathVariable id: String, @PathVariable cantidad: Int): Item {
        return itemService.findById(id, cantidad)
    }

}