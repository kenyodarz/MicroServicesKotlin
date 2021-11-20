package com.bykenyodarz.mskotlin.usuarios.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "usuarios",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("username", "email"))
    ]
)
class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    var idUsuario: String? = null

    @NotBlank
    @Size(max = 20)
    var username: String? = null

    @NotBlank
    var name: String? = null

    @NotBlank
    var lastName: String? = null

    @NotBlank
    @Size(max = 100)
    @Email
    var email: String? = null

    @NotBlank
    @Size(max = 120)
    @JsonIgnore
    var password: String? = null

    private var enabled: Boolean? = null

    private var createdAt: LocalDateTime? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuarios_roles",
        joinColumns = [JoinColumn(name = "id_usuario")],
        inverseJoinColumns = [JoinColumn(name = "id_role")])
    var roles: Set<Role> = HashSet()

    constructor()

    constructor(username: String, name: String, lastName: String, email: String, password: String) {
        this.username = username
        this.name = name
        this.lastName = lastName
        this.email = email
        this.password = password
    }

    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
        enabled = true
    }
}