package com.bykenyodarz.mskotlin.items.models

import java.time.LocalDateTime

data class Product(
    var id: String? = null,
    var name: String? = null,
    var price: Double? = null,
    var createAt: LocalDateTime? = null
)