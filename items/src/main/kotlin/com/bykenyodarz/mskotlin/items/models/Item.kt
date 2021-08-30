package com.bykenyodarz.mskotlin.items.models

data class Item(
    val product: Product? = null,
    val quantity: Int? = null) {


    fun getTotal(): Double {
        return (product!!.price!! * this.quantity!!)
    }

}