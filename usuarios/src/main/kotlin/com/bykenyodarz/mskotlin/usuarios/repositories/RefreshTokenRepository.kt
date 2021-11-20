package com.bykenyodarz.mskotlin.usuarios.repositories

import com.bykenyodarz.mskotlin.usuarios.models.RefreshToken
import com.bykenyodarz.mskotlin.usuarios.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, String> {

    fun findByToken(token: String): Optional<RefreshToken>

    @Modifying
    fun deleteByUser(user: User): Int
    
}