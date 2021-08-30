package com.bykenyodarz.mskotlin.items.services

import com.bykenyodarz.mskotlin.items.models.Item

interface ItemService {

    fun findAll(): List<Item>

    fun findById(id: String, quantity: Int): Item

}