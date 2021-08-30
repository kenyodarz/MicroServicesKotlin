package com.bykenyodarz.mskotlin.items.services.impl

import com.bykenyodarz.mskotlin.items.models.Item
import com.bykenyodarz.mskotlin.items.models.Product
import com.bykenyodarz.mskotlin.items.services.ItemService
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.stream.Collectors

@Service
class ItemServiceImpl(clienteRest: RestTemplate) : ItemService {

    private val clienteRest: RestTemplate

    companion object {
        private const val API_URL = "http://localhost:8001/"
    }


    init {
        clienteRest.also { this.clienteRest = it }
    }

    override fun findAll(): List<Item> {

        val products: List<Product> = this.clienteRest
            .getForObject(API_URL+"all", Array<Product>::class.java)!!
            .toList()

        return products
            .stream()
            .map { Item(it, 1) }
            .collect(Collectors.toList())
    }

    override fun findById(id: String, quantity: Int): Item {
        val pathVariables : MutableMap<String, String>? = null
        pathVariables?.set("id", id)
        val product: Product = this.clienteRest
            .getForObject("http://localhost:8001/$id", Product::class.java, pathVariables)!!

        return Item(product, quantity)

    }

}