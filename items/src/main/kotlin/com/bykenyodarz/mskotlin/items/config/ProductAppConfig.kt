package com.bykenyodarz.mskotlin.items.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class ProductAppConfig {

    @Bean("clienteRest")
    fun registrarRestTemplate(): RestTemplate {
        return RestTemplate()
    }
}