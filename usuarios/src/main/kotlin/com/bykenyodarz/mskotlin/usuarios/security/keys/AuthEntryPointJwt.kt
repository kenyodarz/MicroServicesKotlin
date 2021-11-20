package com.bykenyodarz.mskotlin.usuarios.security.keys

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthEntryPointJwt : AuthenticationEntryPoint {

    private val logger: Logger = LoggerFactory.getLogger(AuthEntryPointJwt::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest, response: HttpServletResponse,
        e: AuthenticationException
    ) {
        logger.error("Usuario no autorizado: {}", e.message)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        val body: MutableMap<String, Any?> = HashMap()
        body["status"] = HttpServletResponse.SC_UNAUTHORIZED
        body["error"] = "Unauthorized"
        body["message"] = e.message
        body["path"] = request.servletPath
        val mapper = ObjectMapper()
        mapper.writeValue(response.outputStream, body)
    }
}