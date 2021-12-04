package com.bykenyodarz.mskotlin.gateway.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain

import reactor.core.publisher.Mono

@Component
class JwtAuthenticationFilter : WebFilter {

    @Autowired
    private val authenticationManager: ReactiveAuthenticationManager? = null


    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return Mono.justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
            .filter { authHeader: String -> authHeader.startsWith("Bearer ") }
            .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
            .map { token: String ->
                token.replace(
                    "Bearer ",
                    ""
                )
            }
            .flatMap { token: String? ->
                authenticationManager!!.authenticate(
                    UsernamePasswordAuthenticationToken(null, token)
                )
            }
            .flatMap { authentication: Authentication ->
                chain.filter(
                    exchange
                ).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
            }
    }
}