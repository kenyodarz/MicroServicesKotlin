package com.bykenyodarz.mskotlin.gateway.filters

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseCookie
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalTime
import java.util.*

@Component
class GlobalFilter : GlobalFilter, Ordered {

    val logger: Logger = LoggerFactory.getLogger(GlobalFilter::class.java)

    override fun filter(exchange: ServerWebExchange?, chain: GatewayFilterChain?): Mono<Void> {
        logger.info("Ejecutando PreFilter")
        val request: ServerHttpRequest = exchange!!.request
        val initialTime: LocalTime? = LocalTime.now()
        for (s in listOf(
            "[${request.method}] request enrutado a ${request.uri} ${request.body}",
            "Global Pre Filter executed"
        )) {
            logger.info(getRequestMessage(exchange))
            logger.info(s)
            exchange.request.mutate().headers {
                it.add("token", "123456")
            }
        }
        return chain!!.filter(exchange).then(Mono.fromRunnable {
            val finalTime: LocalTime? = LocalTime.now()
            logger.info("Tiempo Total: -> {}", Duration.between(initialTime, finalTime).toMillis())
            exchange.response.cookies.add("color",ResponseCookie.from("color", "rojo").build())
            Optional.ofNullable(exchange.request.headers["token"]).ifPresent {
                exchange.response.headers.add("token", it.toString())
             }
            logger.info(getResponseMessage(exchange))
            logger.info("Global Post Filter executed")

        })
    }

    fun getRequestMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val method = request.method
        val path = request.uri.path
        val acceptableMediaTypes = request.headers.accept
        val contentType = request.headers.contentType
        return ">>> $method $path ${HttpHeaders.ACCEPT}: $acceptableMediaTypes ${HttpHeaders.CONTENT_TYPE}: $contentType"
    }

    fun getResponseMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val response = exchange.response
        val method = request.method
        val path = request.uri.path
        val statusCode = getStatus(response)
        val contentType = response.headers.contentType
        return "<<< $method $path HTTP ${statusCode.value()} ${statusCode.reasonPhrase} ${HttpHeaders.CONTENT_TYPE}: $contentType"
    }

    private fun getStatus(response: ServerHttpResponse): HttpStatus = response.statusCode!!

    override fun getOrder(): Int {
        return 1
    }
}