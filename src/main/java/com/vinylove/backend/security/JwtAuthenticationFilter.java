package com.vinylove.backend.security;

import com.vinylove.backend.service.CustomUserDetailsService;
import com.vinylove.backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtre HTTP exécuté une seule fois par requête pour l'authentification JWT.
 * Extrait le token Bearer de l'en-tête {@code Authorization}, le valide,
 * puis peuple le {@link SecurityContextHolder} si le token est valide.
 * Si le token est absent, invalide ou expiré, la requête est simplement transmise
 * sans authentification (Spring Security gérera le refus si la route est protégée).
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Constructeur avec injection des services nécessaires à la validation JWT.
     *
     * @param jwtService               service de génération et validation des tokens JWT
     * @param customUserDetailsService service de chargement des détails utilisateur depuis la base
     */
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Logique principale du filtre : vérifie et traite le token JWT de chaque requête.
     * <p>
     * Étapes :
     * <ol>
     *   <li>Lecture de l'en-tête {@code Authorization} ; passage au filtre suivant si absent ou mal formé</li>
     *   <li>Extraction de l'email depuis le token JWT</li>
     *   <li>Vérification que l'utilisateur n'est pas déjà authentifié dans le contexte courant</li>
     *   <li>Validation du token (signature + expiration) et alimentation du {@link SecurityContextHolder}</li>
     *   <li>En cas d'exception JWT, passage silencieux au filtre suivant sans authentification</li>
     * </ol>
     *
     * @param request     requête HTTP entrante
     * @param response    réponse HTTP
     * @param filterChain chaîne de filtres Spring Security
     * @throws ServletException en cas d'erreur de servlet
     * @throws IOException      en cas d'erreur d'entrée/sortie
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Absence du token ou format non Bearer : on passe sans authentification
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraction du token en supprimant le préfixe "Bearer " (7 caractères)
        String jwt = authHeader.substring(7);

        try {
            String email = jwtService.extractUsername(jwt);

            // N'authentifie que si l'email est présent et qu'aucune authentification n'est déjà en place
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // Enrichit le token d'authentification avec les détails de la requête (IP, session)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token invalide ou expiré : on laisse Spring Security refuser la requête si nécessaire
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
