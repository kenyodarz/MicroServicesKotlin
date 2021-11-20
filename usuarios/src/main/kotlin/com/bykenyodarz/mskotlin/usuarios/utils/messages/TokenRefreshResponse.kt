package com.bykenyodarz.mskotlin.usuarios.utils.messages

class TokenRefreshResponse(var accessToken: String?, var refreshToken: String?) {

    companion object {
        private const val TOKEN_TYPE = "Bearer"
    }

}