package com.bykenyodarz.mskotlin.usuarios.models

import org.hibernate.annotations.GenericGenerator
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "refreshtoken")
class RefreshToken {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    var id: String? = null

    @OneToOne
    var user: User? = null

    @Column(nullable = false, unique = true)
    var token: String? = null

    @Column(nullable = false)
    var expiryDate: Instant? = null
}