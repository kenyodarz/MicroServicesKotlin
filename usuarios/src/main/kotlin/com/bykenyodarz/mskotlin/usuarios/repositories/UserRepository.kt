package com.bykenyodarz.mskotlin.usuarios.repositories

import com.bykenyodarz.mskotlin.usuarios.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, String> {
    fun findByUsername(username: String): Optional<User>

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean

}