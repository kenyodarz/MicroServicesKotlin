package com.bykenyodarz.mskotlin.usuarios.services

import com.bykenyodarz.mskotlin.usuarios.exception.TokenRefreshException
import com.bykenyodarz.mskotlin.usuarios.models.RefreshToken
import com.bykenyodarz.mskotlin.usuarios.repositories.RefreshTokenRepository
import com.bykenyodarz.mskotlin.usuarios.repositories.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import javax.transaction.Transactional


@Service
class RefreshTokenService(refreshTokenRepository: RefreshTokenRepository, userRepository: UserRepository) {

    @Value("\${example.app.jwtRefreshExpirationMs}")
    private val refreshTokenExpirationMs: Long? = null

    private val refreshTokenRepository: RefreshTokenRepository
    private val userRepository: UserRepository

    init {
        refreshTokenRepository.also { this.refreshTokenRepository = it }
        userRepository.also { this.userRepository = it }
    }

    fun findByToken(token: String): Optional<RefreshToken>? {
        return refreshTokenRepository.findByToken(token)
    }

    fun createRefreshToken(userId: String?): RefreshToken? {
        var refreshToken = RefreshToken()
        refreshToken.user = userRepository.findById(userId!!).orElse(null)
        refreshToken.expiryDate = refreshTokenExpirationMs?.let { Instant.now().plusMillis(it) }
        refreshToken.token = UUID.randomUUID().toString()
        refreshToken = refreshTokenRepository.save(refreshToken)
        return refreshToken
    }

    fun verifyExpiration(token: RefreshToken): RefreshToken? {
        if (token.expiryDate!!.compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token)
            throw TokenRefreshException(
                token.token,
                "Refresh token ha expirado. Por favor inicie sesión nuevamente"
            )
        }
        return token
    }

    @Transactional
    fun deleteByUserId(userId: String): Int {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).orElse(null))
    }
}