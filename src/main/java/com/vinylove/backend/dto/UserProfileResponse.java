package com.vinylove.backend.dto;

import com.vinylove.backend.entity.Role;

/**
 * DTO représentant le profil public d'un utilisateur renvoyé au client.
 * N'expose pas le mot de passe ni les tokens ; utilisé pour {@code GET /api/users/me}.
 */
public class UserProfileResponse {

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

    public UserProfileResponse() {
    }

    /**
     * Constructeur complet utilisé lors de la création de la réponse de profil.
     *
     * @param id        identifiant de l'utilisateur
     * @param email     adresse email
     * @param firstName prénom
     * @param lastName  nom de famille
     * @param role      rôle attribué à l'utilisateur
     */
    public UserProfileResponse(Long id, String email, String firstName, String lastName, Role role) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
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
}
