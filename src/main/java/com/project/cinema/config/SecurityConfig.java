package com.project.cinema.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth

                        // ===============================
                        // ROTAS DE USUÁRIO (MAIN)
                        // ===============================
                        .requestMatchers("/api/usuarios/cadastro").permitAll()
                        .requestMatchers("/api/usuarios/login").permitAll()
                        .requestMatchers("/api/usuarios/logout").permitAll()
                        .requestMatchers("/api/usuarios/session-expired").permitAll()

                        // ===============================
                        // ROTAS DE SALAS (MAIN)
                        // ===============================
                        .requestMatchers(HttpMethod.GET, "/api/salas/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/salas/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/salas/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/salas/**").permitAll()

                        // ===============================
                        // ROTAS DE FILMES (SEU TRABALHO)
                        // UC_003 – Listar filmes
                        // UC_009 – Pesquisar filmes
                        // ===============================
                        .requestMatchers(HttpMethod.GET, "/filmes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/filmes").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/filmes/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/filmes/**").permitAll()

                        // ===============================
                        // PADRÃO
                        // ===============================
                        .anyRequest().permitAll()
                )
                .httpBasic(basic -> basic.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .and()
                        .invalidSessionUrl("/api/usuarios/session-expired")
                );

        return http.build();
    }

    // ===============================
    // CONFIGURAÇÃO DE CORS (VUE.JS)
    // ===============================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Content-Type", "Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}