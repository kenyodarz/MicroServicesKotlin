package com.bykenyodarz.mskotlin.items.models

data class Item(
    var product: Product? = null,
    var quantity: Int? = null) {


    fun getTotal(): Double {
        return (product!!.price!! * this.quantity!!)
    }

}