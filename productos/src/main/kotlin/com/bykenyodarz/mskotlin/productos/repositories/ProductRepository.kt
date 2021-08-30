package com.bykenyodarz.mskotlin.productos.repositories

import com.bykenyodarz.mskotlin.productos.models.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: JpaRepository<Product, String> {
}