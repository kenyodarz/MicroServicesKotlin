package com.bykenyodarz.mskotlin.productos.services

import com.bykenyodarz.mskotlin.productos.models.Product

interface IProductService {

    fun findAll(): List<Product>

    fun findById(id: String): Product?

    fun save(product: Product): Product

    fun delete(product: Product)


}