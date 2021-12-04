package com.bykenyodarz.mskotlin.gateway.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.stream.Collectors


@Component
class AuthenticationManagerJWT: ReactiveAuthenticationManager {

    val logger: Logger = LoggerFactory.getLogger(AuthenticationManagerJWT::class.java)


    @Value("\${example.app.jwtSecret}")
    private val jwtSecret: String? = null

    override fun authenticate(authentication: Authentication): Mono<Authentication>? {
        return Mono.just(authentication.credentials.toString())
            .map { token: String? ->
                val llave =
                    Keys.hmacShaKeyFor(jwtSecret!!.toByteArray())
                Jwts.parserBuilder().setSigningKey(llave).build().parseClaimsJws(token).body
            }
            .map { claims: Claims ->
                val username: String = claims.get("username", String::class.java)
                val roles: MutableList<*>? =
                    claims.get("authorities", MutableList::class.java)
                val authorities: Collection<GrantedAuthority> = roles!!.stream()
                    .map { role -> SimpleGrantedAuthority(role.toString()) }
                    .collect(Collectors.toList())
                logger.info("authorities -> {}", authorities)
                UsernamePasswordAuthenticationToken(username, null, authorities)
            }
    }
}