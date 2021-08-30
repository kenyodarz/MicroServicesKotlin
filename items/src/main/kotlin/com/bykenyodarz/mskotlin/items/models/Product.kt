package com.bykenyodarz.mskotlin.items.models

import java.time.LocalDateTime

data class Product(
    val id: String? = null,
    val name: String? = null,
    val price: Double? = null,
    val createAt: LocalDateTime? = null
)