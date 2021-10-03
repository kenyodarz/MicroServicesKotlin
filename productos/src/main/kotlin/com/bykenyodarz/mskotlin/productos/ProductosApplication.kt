package com.bykenyodarz.mskotlin.productos

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class ProductosApplication

fun main(args: Array<String>) {
    runApplication<ProductosApplication>(*args)
}
