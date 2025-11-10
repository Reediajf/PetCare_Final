package com.petcare.PetCare.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // desativa a proteção CSRF (ok em dev, não recomendado em prod)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // permite acesso a todas as rotas sem autenticação
                )
                .httpBasic().disable(); // desativa autenticação básica (sem pedir usuário/senha)

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
