package com.groupeisi.com.dondesang_sn.configurations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/v3/api-docs/**", "/api/swagger-ui.html", "/api/swagger-ui/**").permitAll()
                        .requestMatchers("/api/donneurs/**").permitAll()  // Endpoints donneurs
                        .requestMatchers("/api/rdv/**").permitAll()  // Endpoints RDV
                        .requestMatchers("/api/campagnes/**").permitAll()  // Endpoints campagnes
                        .requestMatchers("/api/CentreCollecte/**").permitAll()  // Endpoints centres de collecte
                        .requestMatchers("/api/hopitals/**").permitAll()  // Endpoints hôpitaux
                        .requestMatchers("/api/dons/**").permitAll()  // Endpoints dons
                        .requestMatchers("/api/demandes/**").permitAll()  // Endpoints demandes
                        .requestMatchers("/api/stockSang/**").permitAll()  // Endpoints stock sang
                        .requestMatchers("/api/utilisateurs/**").permitAll()  // Endpoints utilisateurs
                        .requestMatchers("/api/roles/**").permitAll()  // Endpoints rôles
                        .requestMatchers("/api/cnts/**").permitAll()  // Endpoints CNTS
                        .requestMatchers(HttpMethod.GET, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtAuthFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
                .formLogin(login -> login.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Component
    static class JwtAuthFilter extends org.springframework.web.filter.OncePerRequestFilter {
        private final JwtService jwtService;

        JwtAuthFilter(JwtService jwtService) {
            this.jwtService = jwtService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String path = request.getRequestURI();
            if (path.startsWith("/api/auth")) {
                filterChain.doFilter(request, response);
                return;
            }
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = authHeader.substring(7);
            if (jwtService.isTokenValid(token)) {
                String subject = jwtService.extractSubject(token);
                String role = jwtService.extractClaim(token, claims -> (String) claims.get("role"));
                var authentication = new UsernamePasswordAuthenticationToken(subject, null,
                        role != null && !role.isBlank() ? List.of(new SimpleGrantedAuthority("ROLE_" + role)) : List.of());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }
    }
}
