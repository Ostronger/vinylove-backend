package com.vinylove.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Indique que cette classe contient des configurations de sécurité pour l'application
public class SecurityConfig {

    @Bean // Indique que cette méthode produit un bean géré par Spring, ici un PasswordEncoder pour encoder les mots de passe
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Indique que cette méthode produit un bean géré par Spring, ici une chaîne de filtres de sécurité pour configurer les règles d'accès aux endpoints de l'application
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactive la protection CSRF, car les tokens JWT sont utilisés pour l'authentification
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configure la gestion des sessions pour qu'elle soit sans état, car les tokens JWT sont utilisés pour l'authentification
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users", "/api/users/login").permitAll() // Permet l'accès sans authentification aux endpoints de création de compte et de connexion
                        .anyRequest().authenticated() // Exige une authentification pour tous les autres endpoints
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean // Indique que cette méthode produit un bean géré par Spring, ici un AuthenticationManager pour gérer l'authentification des utilisateurs
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
