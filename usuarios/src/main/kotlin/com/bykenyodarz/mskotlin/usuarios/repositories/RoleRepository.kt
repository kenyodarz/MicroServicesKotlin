package com.bykenyodarz.mskotlin.usuarios.repositories

import com.bykenyodarz.mskotlin.usuarios.models.ERole
import com.bykenyodarz.mskotlin.usuarios.models.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository: JpaRepository<Role, Long> {
    fun findByName(name: ERole): Optional<Role>
}