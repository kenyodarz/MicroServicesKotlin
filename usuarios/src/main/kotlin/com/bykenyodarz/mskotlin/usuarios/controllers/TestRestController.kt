package com.bykenyodarz.mskotlin.usuarios.controllers

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/test")
@Api(tags = ["Prueba"])
class TestRestController {

    @GetMapping("/all")
    fun allAccess(): String {
        return "Contenido Público"
    }

    @GetMapping("/user")
    @ApiOperation(value = "", authorizations = [Authorization(value = "jwtToken")])
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('SUPERVISOR') or hasRole('ADMIN')")
    fun userAccess(): String {
        return "Contenido de Usuario"
    }

    @GetMapping("/supp")
    @ApiOperation(value = "", authorizations = [Authorization(value = "jwtToken")])
    @PreAuthorize("hasRole('SUPERVISOR')")
    fun supAccess(): String {
        return "Contenido de Supervisor"
    }

    @GetMapping("/mod")
    @ApiOperation(value = "", authorizations = [Authorization(value = "jwtToken")])
    @PreAuthorize("hasRole('MODERATOR')")
    fun moderatorAccess(): String {
        return "Contenido de Moderador."
    }

    @GetMapping("/admin")
    @ApiOperation(value = "", authorizations = [Authorization(value = "jwtToken")])
    @PreAuthorize("hasRole('ADMIN')")
    fun adminAccess(): String {
        return "Contenido de Administrador."
    }
}