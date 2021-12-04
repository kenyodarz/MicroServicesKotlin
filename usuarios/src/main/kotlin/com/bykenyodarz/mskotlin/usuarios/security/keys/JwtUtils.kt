package com.bykenyodarz.mskotlin.usuarios.security.keys

import com.bykenyodarz.mskotlin.usuarios.services.UserDetailsServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey


@Component
class JwtUtils(repository: UserDetailsServiceImpl) {
    private val logger: Logger = LoggerFactory.getLogger(JwtUtils::class.java)

    private val repository: UserDetailsServiceImpl

    init {
        repository.also { this.repository = it }
    }

    @Value("\${example.app.jwtSecret}")
    private val jwtSecret: String? = null

    @Value("\${example.app.jwtExpirationMs}")
    private val jwtExpirationMs = 0

    fun generateTokenFromUsername(username: String): String {
        val userPrincipal = repository.loadUserByUsername(username)
        val key: SecretKey = Keys.hmacShaKeyFor(jwtSecret!!.toByteArray())
        val oMapper = ObjectMapper()
        val map: MutableMap<String, Any> = oMapper.convertValue(userPrincipal, MutableMap::class.java) as MutableMap<String, Any>
        return Jwts.builder()
            .setClaims(map)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(key)
            .compact()
    }

    fun getUsernameFromJwtToken(token: String): String {
        val key: SecretKey = Keys.hmacShaKeyFor(jwtSecret!!.toByteArray())
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun validateJwtToken(authToken: String): Boolean {
        val key: SecretKey = Keys.hmacShaKeyFor(jwtSecret!!.toByteArray())
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authToken)
            return true
        } catch (e: InvalidClaimException) {
            logger.error("El campo 'subject' faltaba o no tenía un valor : {${e.message}}")
        } catch (e: MalformedJwtException) {
            logger.error("Token JWT invalido: {${e.message}}")
        } catch (e: ExpiredJwtException) {
            logger.error("El token JWT ha expirado: {${e.message}}")
        } catch (e: UnsupportedJwtException) {
            logger.error("El token JWT no es soportado: {${e.message}}")
        } catch (e: IllegalArgumentException) {
            logger.error("La cadena de caracteres del JWT esta vacía: {${e.message}}")
        } catch (e: MissingClaimException) {
            logger.error("El JWT analizado no tenía el subcampo: {${e.message}}")
        } catch (e: IncorrectClaimException) {
            logger.error("El JWT analizado tenía un subcampo, pero su valor no era igual a 'usuario': {${e.message}}")
        }
        return false
    }

    fun parameters(obj: Any): Map<String, Any>? {
        val map: MutableMap<String, Any> = HashMap()
        for (field in obj.javaClass.declaredFields) {
            field.isAccessible = true
            try {
                map[field.name] = field[obj]
            } catch (e: Exception) {
                logger.error("Error by ${e.message}")
            }
        }
        return map
    }
}