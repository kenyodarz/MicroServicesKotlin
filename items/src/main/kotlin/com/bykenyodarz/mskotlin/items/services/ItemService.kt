package com.bykenyodarz.mskotlin.items.services

import com.bykenyodarz.mskotlin.items.models.Item
import com.bykenyodarz.mskotlin.items.models.Product

interface ItemService {

    fun findAll(): List<Item>

    fun findById(id: String, quantity: Int): Item

    fun save(product: Product) : Product

    fun delete(id: String)

}