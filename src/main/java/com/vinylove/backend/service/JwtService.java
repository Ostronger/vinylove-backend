package com.vinylove.backend.service;

import com.vinylove.backend.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;



@Service // cette annotation indique que cette classe est un service Spring, ce qui permet à Spring de la détecter et de l'injecter là où elle est nécessaire
public class JwtService {
    
    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(email) // définit le sujet du token, ici l'email de l'utilisateur
                .claim("role", role) // ajoute une réclamation personnalisée pour le rôle de l'utilisateur
                .issuedAt(now) // définit la date d'émission du token
                .expiration(expiryDate) // définit la date d'expiration du token
                .signWith(getSigningKey()) // signe le token avec la clé secrète
                .compact(); // génère le token sous forme de chaîne compacte
    }

    public String extractUsername(String token) { // extrait le nom d'utilisateur (email) du token
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String email) { // vérifie si le token est valide en comparant le nom d'utilisateur extrait du token avec l'email fourni et en vérifiant que le token n'est pas expiré
        String username = extractUsername(token);
        return (username.equals(email) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) { // vérifie si le token est expiré en comparant la date d'expiration du token avec la date actuelle
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) { // extrait toutes les réclamations du token en le parsant avec la clé secrète
        return Jwts.parser()
                .verifyWith(getSigningKey()) // vérifie la signature du token avec la clé secrète
                .build()
                .parseSignedClaims(token) // parse le token signé et extrait les réclamations
                .getPayload(); // retourne les réclamations extraites du token
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractRole(String token) { // extrait le rôle de l'utilisateur à partir du token en accédant à la réclamation personnalisée "role"
        return extractAllClaims(token).get("role", String.class);
    }


}
