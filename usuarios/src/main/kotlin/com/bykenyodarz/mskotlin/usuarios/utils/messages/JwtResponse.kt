package com.bykenyodarz.mskotlin.usuarios.utils.messages

class JwtResponse(
    var token: String?,
    var refreshToken: String?,
    var id: String?,
    var name: String?,
    var lastName: String?,
    var username: String?,
    var email: String?,
    var roles: List<String>?
) {

    companion object {
        private const val TOKEN_TYPE = "Bearer"
    }



}