package com.vinylove.backend.dto;

/**
 * DTO représentant la demande de déconnexion d'un utilisateur.
 * Reçu dans le corps de la requête {@code POST /api/users/logout}.
 * Le refresh token fourni sera révoqué pour invalider la session.
 */
public class LogoutRequest {

    /** Refresh token à révoquer pour invalider la session de l'utilisateur. */
    private String refreshToken;

    public LogoutRequest() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
