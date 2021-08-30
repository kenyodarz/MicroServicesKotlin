package com.bykenyodarz.mskotlin.productos.services.impl

import com.bykenyodarz.mskotlin.productos.models.Product
import com.bykenyodarz.mskotlin.productos.repositories.ProductRepository
import com.bykenyodarz.mskotlin.productos.services.IProductService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductServiceImpl(repository: ProductRepository) : IProductService {

    private val repository: ProductRepository

    init {
        repository.also { this.repository = it }
    }

    @Transactional(readOnly = true)
    override fun findAll(): List<Product> {
        return this.repository.findAll()
    }

    @Transactional(readOnly = true)
    override fun findById(id: String): Product? {
        val product = this.repository.findById(id)
        return product.orElse(null)
    }

    override fun save(product: Product): Product {
        return this.repository.save(product)
    }

    override fun delete(product: Product) {
        this.repository.delete(product)
    }
}