package com.bykenyodarz.mskotlin.items.services.impl

import com.bykenyodarz.mskotlin.items.models.Item
import com.bykenyodarz.mskotlin.items.models.Product
import com.bykenyodarz.mskotlin.items.services.ItemService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.stream.Collectors

@Service
class ItemServiceImpl(clienteRest: RestTemplate) : ItemService {

    private val clienteRest: RestTemplate

    companion object {
        private const val API_URL = "http://servicio-productos/"
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
            .getForObject(API_URL + id, Product::class.java, pathVariables)!!

        return Item(product, quantity)

    }

    override fun save(product: Product): Product {
        val body: HttpEntity<Product> = HttpEntity(product)
        val response = clienteRest.exchange(API_URL + "save", HttpMethod.POST, body, Product::class.java)
        return response.body!!
    }

    override fun delete(id: String) {
        val pathVariables : MutableMap<String, String>? = null
        pathVariables?.set("id", id)
        clienteRest.delete(API_URL + "delete/$id", pathVariables)
    }

}