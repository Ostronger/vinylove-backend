package com.vinylove.backend.config;

import com.vinylove.backend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // cette annotation indique que cette classe contient des configurations Spring, ce qui permet à Spring de la détecter et de l'utiliser pour configurer les beans et les paramètres de sécurité
@EnableWebSecurity // cette annotation active la sécurité web de Spring Security, ce qui permet de configurer les règles de sécurité pour les requêtes HTTP entrantes
@EnableConfigurationProperties(JwtProperties.class) // cette annotation indique que la classe JwtProperties doit être utilisée pour charger les propriétés de configuration liées au JWT, ce qui permet d'injecter ces propriétés dans les beans qui en ont besoin
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // ce filtre est utilisé pour intercepter les requêtes HTTP entrantes, extraire le token JWT de l'en-tête d'autorisation, valider le token et configurer le contexte de sécurité si le token est valide

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) { // ce constructeur est utilisé pour injecter le JwtAuthenticationFilter dans la configuration de sécurité, ce qui permet de l'ajouter à la chaîne de filtres de Spring Security
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean // cette annotation indique que cette méthode retourne un bean qui doit être géré par le conteneur Spring, ce qui permet d'injecter ce bean dans d'autres parties de l'application où un PasswordEncoder est nécessaire
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // cette annotation indique que cette méthode retourne un bean qui doit être géré par le conteneur Spring, ce qui permet d'injecter ce bean dans d'autres parties de l'application où une SecurityFilterChain est nécessaire pour configurer les règles de sécurité
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
