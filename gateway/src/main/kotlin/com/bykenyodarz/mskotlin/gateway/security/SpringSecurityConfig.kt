package com.bykenyodarz.mskotlin.gateway.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@EnableWebFluxSecurity
class SpringSecurityConfig {


    @Autowired
    private val authenticationFilter: JwtAuthenticationFilter? = null

    @Bean
    fun configure(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.authorizeExchange()
            .pathMatchers("/api/usuarios/auth/**").permitAll()
            .pathMatchers(
                HttpMethod.GET, "/api/productos/all", "/api/items/all"
            ).permitAll()
            .pathMatchers(HttpMethod.GET, "/api/usuarios/test/**").permitAll()
            .anyExchange()
            .authenticated()
            .and()
            .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .csrf().disable()
            .build()
    }

}