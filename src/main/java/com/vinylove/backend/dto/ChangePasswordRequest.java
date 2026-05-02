package com.vinylove.backend.dto;

/**
 * DTO représentant la demande de changement de mot de passe d'un utilisateur.
 * Reçu dans le corps de la requête {@code PUT /api/users/me/password}.
 */
public class ChangePasswordRequest {

    /** Mot de passe actuellement en vigueur, utilisé pour vérification avant modification. */
    private String currentPassword;

    /** Nouveau mot de passe qui remplacera l'actuel après validation. */
    private String newPassword;

    public ChangePasswordRequest() {
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
