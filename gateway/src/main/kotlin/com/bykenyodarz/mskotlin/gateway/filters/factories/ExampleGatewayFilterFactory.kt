package com.bykenyodarz.mskotlin.gateway.filters.factories

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class ExampleGatewayFilterFactory() :
    AbstractGatewayFilterFactory<ExampleGatewayFilterFactory.Config>(Config::class.java) {

    val logger: Logger = LoggerFactory.getLogger(ExampleGatewayFilterFactory::class.java)



    override fun apply(config: Config?): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            logger.info("Ejecutando pre Gateway filter factory: ${config!!.message}")
            chain!!.filter(exchange).then(Mono.fromRunnable {

                Optional.ofNullable(config.cookieValue).ifPresent {
                    exchange.response.addCookie(ResponseCookie.from(config.cookieName!!, it).build())
                }

                logger.info("Ejecutando post Gateway filter factory: ${config.message}")

            })
        }
    }

     class Config {
        var message: String? = null
        var cookieValue: String? = null
        var cookieName: String? = null
    }

}