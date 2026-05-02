package com.vinylove.backend.dto;

/**
 * DTO représentant les identifiants de connexion envoyés par le client.
 * Reçu dans le corps de la requête {@code POST /api/users/login}.
 */
public class LoginRequest {

    /** Adresse email de l'utilisateur servant d'identifiant unique. */
    private String email;

    /** Mot de passe en clair de l'utilisateur, comparé au hash BCrypt stocké en base. */
    private String password;

    public LoginRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
