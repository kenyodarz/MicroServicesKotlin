package com.bykenyodarz.mskotlin.usuarios.models

import org.bouncycastle.asn1.x500.style.RFC4519Style.name
import javax.persistence.*

@Entity
@Table(name = "roles")
class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idRole: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    var name: ERole? = null
}