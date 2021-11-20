package com.bykenyodarz.mskotlin.usuarios.utils.request

import javax.validation.constraints.NotBlank

class TokenRefreshRequest {
    @NotBlank
    var refreshToken: String? = null
}