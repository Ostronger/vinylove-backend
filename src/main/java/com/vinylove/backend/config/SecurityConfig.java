package com.vinylove.backend.config;

import com.vinylove.backend.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

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

/**
 * Configuration principale de la sécurité Spring Security.
 * Définit la chaîne de filtres HTTP, les règles d'autorisation par route et par rôle,
 * la politique de session sans état (stateless), et enregistre le filtre JWT.
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    /** Filtre JWT injecté pour valider le token à chaque requête entrante. */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructeur avec injection du filtre JWT.
     *
     * @param jwtAuthenticationFilter filtre responsable de l'extraction et de la validation du JWT
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Déclare le bean {@link PasswordEncoder} utilisant l'algorithme BCrypt.
     * Utilisé pour hacher et vérifier les mots de passe des utilisateurs.
     *
     * @return une instance de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure et retourne la chaîne de filtres de sécurité HTTP.
     * <p>
     * Règles appliquées :
     * <ul>
     *   <li>CSRF désactivé (API REST stateless)</li>
     *   <li>Sessions non créées côté serveur (STATELESS)</li>
     *   <li>Routes publiques : inscription, connexion, refresh-token, logout</li>
     *   <li>Routes authentifiées : profil personnel, changement de mot de passe</li>
     *   <li>Routes réservées ADMIN : gestion des événements et des utilisateurs</li>
     * </ul>
     *
     * @param http objet de configuration Spring Security
     * @return la {@link SecurityFilterChain} construite
     * @throws Exception en cas d'erreur lors de la configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        // Retourne 401 si la requête n'est pas authentifiée
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                        // Retourne 403 si l'utilisateur n'a pas les droits suffisants
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
                )
                .authorizeHttpRequests(auth -> auth
                        // Endpoints publics accessibles sans authentification
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/check-in").permitAll()

                        // Endpoints publics d'authentification
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/refresh-token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/logout").permitAll()

                        // Endpoints accessibles à tout utilisateur authentifié
                        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/me/password").authenticated()

                        // Lecture des événements  et guests réservée aux utilisateurs connectés
                        .requestMatchers(HttpMethod.GET, "/api/events").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/events/**").authenticated()

                        // Check-in des invités réservé au personnel (ADMIN et STAFF)
                        .requestMatchers(HttpMethod.POST, "/api/events/*/guests/check-in").hasAnyRole("ADMIN", "STAFF")

                        // Création et suppression d'événements et guests réservées aux administrateurs
                        .requestMatchers(HttpMethod.POST, "/api/events/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/**").hasRole("ADMIN")

                        // Mise à jour d'événements et guests réservée aux administrateurs
                        .requestMatchers(HttpMethod.PUT, "/api/events/**").hasRole("ADMIN")

                        // Gestion des utilisateurs réservée aux administrateurs
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                // Le filtre JWT s'exécute avant le filtre d'authentification standard de Spring
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
