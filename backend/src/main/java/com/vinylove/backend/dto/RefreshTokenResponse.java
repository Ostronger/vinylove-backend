package com.vinylove.backend.dto;

/**
 * DTO représentant la réponse renvoyée après un rafraîchissement de token réussi.
 * Contient le nouvel access token JWT ainsi que le refresh token existant (inchangé).
 */
public class RefreshTokenResponse {

    /** Nouvel access token JWT généré pour prolonger la session. */
    private String accessToken;

    /** Refresh token existant retourné tel quel (non renouvelé à cette étape). */
    private String refreshToken;

    public RefreshTokenResponse() {
    }

    /**
     * Constructeur utilisé lors de la création de la réponse de rafraîchissement.
     *
     * @param accessToken  nouvel access token JWT
     * @param refreshToken refresh token existant
     */
    public RefreshTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
