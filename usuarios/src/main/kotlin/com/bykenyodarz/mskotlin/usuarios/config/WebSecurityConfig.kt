package com.bykenyodarz.mskotlin.usuarios.config

import com.bykenyodarz.mskotlin.usuarios.security.keys.AuthEntryPointJwt
import com.bykenyodarz.mskotlin.usuarios.security.keys.AuthTokenFilter
import com.bykenyodarz.mskotlin.usuarios.services.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.config.annotation.web.builders.WebSecurity





@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    var userDetailsService: UserDetailsServiceImpl? = null

    @Autowired
    var unauthorizedHandle: AuthEntryPointJwt? = null

    @Bean
    fun authenticationJwtTokenFilter(): AuthTokenFilter {
        return AuthTokenFilter()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(java.lang.Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

    @Throws(Exception::class)
    override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder
            .userDetailsService<UserDetailsServiceImpl>(userDetailsService)
            .passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandle).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .antMatchers("/auth/**").permitAll()
            .antMatchers("/test/**").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/configuration/**").permitAll()
            .antMatchers("/swagger-resources/**").permitAll()
            .antMatchers("/v2/api-docs").permitAll()
            .anyRequest().authenticated()
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}