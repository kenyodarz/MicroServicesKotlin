package com.bykenyodarz.mskotlin.items

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ItemsApplication

fun main(args: Array<String>) {
    runApplication<ItemsApplication>(*args)
}
