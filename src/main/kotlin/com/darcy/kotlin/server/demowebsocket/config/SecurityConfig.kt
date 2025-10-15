package com.darcy.kotlin.server.demowebsocket.config

import com.darcy.kotlin.server.demowebsocket.utils.PasswordUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/api/users/create",
                    "/api/login",
                ).permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic {

            }
            .csrf {
                it.disable() // 通常为API禁用CSRF
            }
            .build()
    }
}