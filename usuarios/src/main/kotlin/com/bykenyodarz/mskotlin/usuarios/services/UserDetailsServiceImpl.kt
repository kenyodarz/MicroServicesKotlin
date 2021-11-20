package com.bykenyodarz.mskotlin.usuarios.services

import com.bykenyodarz.mskotlin.usuarios.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserDetailsServiceImpl(repository: UserRepository) : UserDetailsService {
    private val repository: UserRepository

    init {
        repository.also { this.repository = it }
    }

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = repository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("No se ha encontrado el usuario: $username") }
        return UserDetailsImpl.build(user)
    }
}