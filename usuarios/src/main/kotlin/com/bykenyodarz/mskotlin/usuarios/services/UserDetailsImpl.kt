package com.bykenyodarz.mskotlin.usuarios.services

import com.bykenyodarz.mskotlin.usuarios.models.Role
import com.bykenyodarz.mskotlin.usuarios.models.User
import com.fasterxml.jackson.annotation.JsonIgnore
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

class UserDetailsImpl(
    private var id: String,
    private var name: String,
    private var lastname: String,
    private var username: String,
    private var email: String,
    @JsonIgnore
    private var password: String,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {


    companion object {
        private const val serialVersionUID = 1L

        fun build(user: User): UserDetailsImpl {
            val authorities: List<GrantedAuthority> = user.roles.stream()
                .map { role: Role -> SimpleGrantedAuthority(role.name!!.name) }
                .collect(Collectors.toList())
            return UserDetailsImpl(
                user.idUsuario!!,
                user.name!!,
                user.lastName!!,
                user.username!!,
                user.email!!,
                user.password!!,
                authorities
            )

        }
    }


    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    fun getId(): String {
        return id
    }

    fun getName(): String {
        return name
    }

    fun getEmail(): String {
        return email
    }

    fun getLastName(): String {
        return lastname
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}