package com.vinylove.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Classe de configuration qui charge les propriétés JWT depuis le fichier application.properties.
 * Les propriétés sont préfixées par {@code jwt} (ex : {@code jwt.secret}, {@code jwt.expiration}).
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /** Clé secrète encodée en Base64 utilisée pour signer les tokens JWT. */
    private String secret;

    /** Durée de validité du token JWT en millisecondes. */
    private long expiration;

    /**
     * Retourne la clé secrète utilisée pour signer les tokens JWT.
     *
     * @return la clé secrète en Base64
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Définit la clé secrète utilisée pour signer les tokens JWT.
     *
     * @param secret la clé secrète en Base64
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * Retourne la durée de validité du token JWT.
     *
     * @return la durée en millisecondes
     */
    public long getExpiration() {
        return expiration;
    }

    /**
     * Définit la durée de validité du token JWT.
     *
     * @param expiration la durée en millisecondes
     */
    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
