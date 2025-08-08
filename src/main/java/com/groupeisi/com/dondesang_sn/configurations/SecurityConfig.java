package com.groupeisi.com.dondesang_sn.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // désactiver CSRF pour les tests
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // tout est accessible sans login
                )
                .formLogin(login -> login.disable()) // pas de formulaire HTML
                .httpBasic(basic -> basic.disable()); // pas de Basic Auth

        return http.build();
    }
}
