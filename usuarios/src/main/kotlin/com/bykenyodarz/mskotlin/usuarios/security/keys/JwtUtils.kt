package com.bykenyodarz.mskotlin.usuarios.security.keys

import com.bykenyodarz.mskotlin.usuarios.services.UserDetailsImpl
import io.jsonwebtoken.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtUtils {
    private val logger: Logger = LoggerFactory.getLogger(JwtUtils::class.java)

    @Value("\${example.app.jwtSecret}")
    private val jwtSecret: String? = null

    @Value("\${example.app.jwtExpirationMs}")
    private val jwtExpirationMs = 0

    fun generateTokenFromUsername(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun getUsernameFromJwtToken(token: String): String {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
    }

    fun validateJwtToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
            return true
        } catch (e: SignatureException) {
            logger.error("Firma invalida en el JWT: {${e.message}}")
        } catch (e: MalformedJwtException) {
            logger.error("Token JWT invalido: {${e.message}}")
        } catch (e: ExpiredJwtException) {
            logger.error("El token JWT ha expirado: {${e.message}}")
        } catch (e: UnsupportedJwtException) {
            logger.error("El token JWT no es soportado: {${e.message}}")
        } catch (e: IllegalArgumentException) {
            logger.error("La cadena de caracteres del JWT esta vacía: {${e.message}}")
        }

        return false
    }
}