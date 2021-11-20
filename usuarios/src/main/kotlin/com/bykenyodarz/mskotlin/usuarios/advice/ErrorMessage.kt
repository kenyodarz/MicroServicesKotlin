package com.bykenyodarz.mskotlin.usuarios.advice

import java.time.LocalDateTime

class ErrorMessage(
    var statusCode: Int?,
    var timestamp: LocalDateTime?,
    var message: String?,
    var description: String?
)