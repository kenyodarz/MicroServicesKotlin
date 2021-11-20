package com.bykenyodarz.mskotlin.usuarios.controllers

import com.bykenyodarz.mskotlin.usuarios.exception.TokenRefreshException
import com.bykenyodarz.mskotlin.usuarios.models.ERole
import com.bykenyodarz.mskotlin.usuarios.models.RefreshToken
import com.bykenyodarz.mskotlin.usuarios.models.Role
import com.bykenyodarz.mskotlin.usuarios.models.User
import com.bykenyodarz.mskotlin.usuarios.repositories.RoleRepository
import com.bykenyodarz.mskotlin.usuarios.repositories.UserRepository
import com.bykenyodarz.mskotlin.usuarios.security.keys.JwtUtils
import com.bykenyodarz.mskotlin.usuarios.services.RefreshTokenService
import com.bykenyodarz.mskotlin.usuarios.services.UserDetailsImpl
import com.bykenyodarz.mskotlin.usuarios.utils.messages.JwtResponse
import com.bykenyodarz.mskotlin.usuarios.utils.messages.MessageResponse
import com.bykenyodarz.mskotlin.usuarios.utils.messages.TokenRefreshResponse
import com.bykenyodarz.mskotlin.usuarios.utils.request.LogOutRequest
import com.bykenyodarz.mskotlin.usuarios.utils.request.LoginRequest
import com.bykenyodarz.mskotlin.usuarios.utils.request.SignupRequest
import com.bykenyodarz.mskotlin.usuarios.utils.request.TokenRefreshRequest
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import javax.validation.Valid


@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/auth")
@Api(tags = ["Autentificación"])
class AuthRestController(
    private val authenticationManager: AuthenticationManager, private val userRepository: UserRepository,
    private val roleRepository: RoleRepository, private val encoder: PasswordEncoder, private val jwtUtils: JwtUtils,
    private val refreshTokenService: RefreshTokenService
) {
    // Validator de campos
    fun validar(result: BindingResult): ResponseEntity<Any> {
        val errores: MutableMap<String, Any> = HashMap()
        result.fieldErrors.forEach { err ->
            run {
                errores[err.field] = "El Campo ${err.field} ${err.defaultMessage}"
            }
        }
        return ResponseEntity.badRequest().body(errores)
    }

    @PostMapping("/signin")
    @ApiOperation(value = "Iniciar Sesión", notes = "Servicio que nos valida la información de usuario e inicia sesión")
    @ApiResponses(
        value = [ApiResponse(
            code = 200,
            message = "Usuario inicia sesión correctamente"
        ), ApiResponse(code = 201, message = "Usuario inicia sesión correctamente"), ApiResponse(
            code = 400,
            message = "Campos Inválidos"
        ), ApiResponse(code = 401, message = "Usuario no autorizado"), ApiResponse(
            code = 403,
            message = "Recurso no disponible"
        ), ApiResponse(code = 404, message = "Recurso no encontrado")]
    )
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest, result: BindingResult): ResponseEntity<Any> {
        if (result.hasErrors()) return validar(result)
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username!!, loginRequest.password!!))
        SecurityContextHolder.getContext().authentication = authentication
        val userDetails = authentication.principal as UserDetailsImpl
        val jwtToken = jwtUtils.generateTokenFromUsername(userDetails.username)
        val roles = userDetails.authorities.stream()
            .map { item: GrantedAuthority -> item.authority } .collect(Collectors.toList())
        val refreshToken = refreshTokenService.createRefreshToken(userDetails.getId())
        return ResponseEntity.ok(
            JwtResponse(
                jwtToken,
                refreshToken!!.token,
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getLastName(),
                userDetails.username,
                userDetails.getEmail(),
                roles
            )
        )
    }

    @PostMapping("/signup")
    @ApiOperation(value = "Registrar Usuario", notes = "Servicio que crea los usuario para el aplicativo")
    @ApiResponses(
        value = [ApiResponse(code = 200, message = "Usuario creado correctamente"), ApiResponse(
            code = 201,
            message = "Usuario creado correctamente"
        ), ApiResponse(code = 400, message = "Campos Inválidos(Duplicados o incorrectos)"), ApiResponse(
            code = 401,
            message = "Usuario no autorizado"
        ), ApiResponse(code = 403, message = "Recurso no disponible"), ApiResponse(
            code = 404,
            message = "Recurso no encontrado"
        )]
    )
    fun registerUser(@RequestBody signUpRequest: @Valid SignupRequest, result: BindingResult): ResponseEntity<Any> {
        if (result.hasErrors()) return validar(result)
        if (userRepository.existsByUsername(signUpRequest.username!!)) {
            return ResponseEntity
                .badRequest()
                .body(MessageResponse("Error: Este nombre de usuario ya existe!"))
        }
        if (userRepository!!.existsByEmail(signUpRequest.email!!)) {
            return ResponseEntity
                .badRequest()
                .body(MessageResponse("Error: Este Email ya esta en uso!"))
        }
        // Creación de un Nuevo Usuario
        val user = User(
            signUpRequest.username!!,
            signUpRequest.name!!,
            signUpRequest.lastName!!,
            signUpRequest.email!!,
            encoder!!.encode(signUpRequest.password)
        )
        val strRoles: MutableSet<String>? = signUpRequest.roles
        val roles: MutableSet<Role> = HashSet()
        if (strRoles == null) {
            val userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow { RuntimeException("Error: Rol user no Encontrado.") }
            roles.add(userRole)
        } else {
            strRoles.forEach { role ->
                when (role) {
                    "admin" -> {
                        val adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow { RuntimeException("Error: Rol admin no Encontrado.") }
                        roles.add(adminRole)
                    }
                    "mod" -> {
                        val modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                            .orElseThrow { RuntimeException("Error: Rol mod no Encontrado.") }
                        roles.add(modRole)
                    }
                    "sup" -> {
                        val supRole = roleRepository.findByName(ERole.ROLE_SUPERVISOR)
                            .orElseThrow { RuntimeException("EError: Rol sup no Encontrado.") }
                        roles.add(supRole)
                    }
                    else -> {
                        val userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow { RuntimeException("Error: Rol user no Encontrado.") }
                        roles.add(userRole)
                    }
                }
            }
        }
        user.roles = roles
        userRepository.save(user)
        return ResponseEntity.ok(MessageResponse("Usuario creado Correctamente"))
    }

    @PostMapping("/refreshtoken")
    fun refreshToken(@RequestBody request: @Valid TokenRefreshRequest?): ResponseEntity<*>? {
        val requestRefreshToken = request!!.refreshToken
        return refreshTokenService.findByToken(requestRefreshToken!!)
            ?.map { token: RefreshToken? ->
                refreshTokenService.verifyExpiration(
                    token!!
                )
            }
            ?.map<User> { it!!.user }
            ?.map { user: User ->
                val token = jwtUtils.generateTokenFromUsername(
                    user.username!!
                )
                ResponseEntity.ok(
                    TokenRefreshResponse(
                        token,
                        requestRefreshToken
                    )
                )
            }
            ?.orElseThrow {
                TokenRefreshException(
                    requestRefreshToken,
                    "Refresh token no esta en la Base de datos!"
                )
            }
    }

    @PostMapping("/logout")
    fun logoutUser(@RequestBody logOutRequest: @Valid LogOutRequest?): ResponseEntity<*> {
        logOutRequest!!.userId?.let { refreshTokenService.deleteByUserId(it) }
        return ResponseEntity.ok(MessageResponse("Log out successful!"))
    }
}
