package com.vinylove.backend.dto;

/**
 * DTO représentant les données de mise à jour du profil d'un utilisateur.
 * Reçu dans le corps de la requête {@code PUT /api/users/me}.
 */
public class UpdateUserProfileRequest {

    /** Nouvelle adresse email ; doit être unique dans la base de données. */
    private String email;

    /** Nouveau prénom de l'utilisateur. */
    private String firstName;

    /** Nouveau nom de famille de l'utilisateur. */
    private String lastName;

    public UpdateUserProfileRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
