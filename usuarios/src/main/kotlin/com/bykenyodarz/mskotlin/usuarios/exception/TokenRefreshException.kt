package com.bykenyodarz.mskotlin.usuarios.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class TokenRefreshException(token: String?, message: String?) :
    RuntimeException(String.format("Ha fallado por [%s]: %s", token, message)) {
    companion object {
        private const val serialVersionUID = 1L
    }
}