package com.vinylove.backend.dto;

/**
 * DTO représentant la demande de rafraîchissement du token d'accès JWT.
 * Reçu dans le corps de la requête {@code POST /api/users/refresh-token}.
 */
public class RefreshTokenRequest {

    /** Refresh token valide permettant de générer un nouvel access token JWT. */
    private String refreshToken;

    public RefreshTokenRequest() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
