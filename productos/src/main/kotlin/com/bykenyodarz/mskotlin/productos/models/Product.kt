package com.bykenyodarz.mskotlin.productos.models

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "`productos")
class Product: Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    var id: String? = null

    @NotBlank
    @Column(name = "nombre")
    var name: String? = null

    @NotNull
    @Column(name = "precio")
    var price: Double? = null

    @Column(name = "create_at")
    var createAt: LocalDateTime? = null

    @Transient
    var port: Int? = null

    @PrePersist
    fun prePersist() {
        createAt = LocalDateTime.now()
    }

}