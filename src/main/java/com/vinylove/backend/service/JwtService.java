package com.vinylove.backend.service;

import com.vinylove.backend.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Service responsable de la génération, de la validation et de l'extraction des données
 * depuis les tokens JWT (JSON Web Tokens).
 * Utilise la clé secrète et la durée d'expiration définies dans {@link JwtProperties}.
 */
@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    /**
     * Constructeur avec injection des propriétés JWT.
     *
     * @param jwtProperties propriétés de configuration JWT (clé secrète, expiration)
     */
    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * Génère un token JWT signé contenant l'email et le rôle de l'utilisateur.
     *
     * @param email adresse email de l'utilisateur, utilisée comme sujet du token
     * @param role  rôle de l'utilisateur ajouté en tant que claim personnalisé
     * @return token JWT compact sous forme de chaîne de caractères
     */
    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrait l'email (sujet) depuis un token JWT.
     *
     * @param token token JWT à analyser
     * @return adresse email contenue dans le sujet du token
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Vérifie qu'un token JWT est valide : le sujet doit correspondre à l'email fourni
     * et le token ne doit pas être expiré.
     *
     * @param token token JWT à valider
     * @param email email attendu dans le sujet du token
     * @return {@code true} si le token est valide, {@code false} sinon
     */
    public boolean isTokenValid(String token, String email) {
        String username = extractUsername(token);
        return (username.equals(email) && !isTokenExpired(token));
    }

    /**
     * Vérifie si la date d'expiration du token est antérieure à la date actuelle.
     *
     * @param token token JWT à vérifier
     * @return {@code true} si le token est expiré, {@code false} sinon
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * Parse et extrait l'ensemble des claims (données) d'un token JWT signé.
     * Vérifie la signature avec la clé secrète au passage.
     *
     * @param token token JWT à parser
     * @return objet {@link Claims} contenant toutes les données du payload
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Décode la clé secrète Base64 et retourne la clé HMAC-SHA utilisée pour signer les tokens.
     *
     * @return clé secrète {@link SecretKey} dérivée de la configuration
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrait le rôle de l'utilisateur depuis les claims personnalisés du token JWT.
     *
     * @param token token JWT à analyser
     * @return rôle de l'utilisateur sous forme de chaîne (ex : "ADMIN", "STAFF")
     */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
}
