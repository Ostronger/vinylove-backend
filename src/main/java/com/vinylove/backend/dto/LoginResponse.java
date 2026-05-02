package com.vinylove.backend.dto;

import com.vinylove.backend.entity.Role;

/**
 * DTO représentant la réponse renvoyée au client après une connexion réussie.
 * Contient les informations publiques de l'utilisateur, un access token JWT
 * et un refresh token pour le renouvellement de session.
 */
public class LoginResponse {

    /** Identifiant unique de l'utilisateur. */
    private Long id;

    /** Adresse email de l'utilisateur. */
    private String email;

    /** Prénom de l'utilisateur. */
    private String firstName;

    /** Nom de famille de l'utilisateur. */
    private String lastName;

    /** Rôle de l'utilisateur (ADMIN ou STAFF). */
    private Role role;

    /** Message de confirmation de la connexion. */
    private String message;

    /** Token JWT d'accès à courte durée de vie, à inclure dans les requêtes suivantes. */
    private String accessToken;

    /** Token de rafraîchissement à longue durée de vie, utilisé pour obtenir un nouvel access token. */
    private String refreshToken;

    public LoginResponse() {
    }

    /**
     * Constructeur complet utilisé lors de la création de la réponse de connexion.
     *
     * @param id           identifiant de l'utilisateur
     * @param email        adresse email de l'utilisateur
     * @param firstName    prénom de l'utilisateur
     * @param lastName     nom de famille de l'utilisateur
     * @param role         rôle de l'utilisateur
     * @param message      message de confirmation
     * @param accessToken  token JWT d'accès
     * @param refreshToken token de rafraîchissement
     */
    public LoginResponse(Long id, String email, String firstName, String lastName, Role role, String message, String accessToken, String refreshToken) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
