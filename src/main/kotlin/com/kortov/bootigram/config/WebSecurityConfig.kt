package com.kortov.bootigram.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
@EnableWebFluxSecurity
class WebSecurityConfig {
    @Bean
    fun securitygWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.authorizeExchange()
                .pathMatchers("/", TelegramProperties.WEB_HOOK, "/foo")
                .permitAll()
                .and()
                .csrf()
                .disable()
                .build()
    }
}