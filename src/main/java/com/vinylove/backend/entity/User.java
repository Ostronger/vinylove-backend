package com.vinylove.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entité JPA représentant un utilisateur de l'application Vinylove.
 * Mappée sur la table {@code users} en base de données.
 * Le mot de passe est exclu de la sérialisation JSON sortante pour ne pas l'exposer dans les réponses API.
 */
@Entity
@Table(name = "users")
public class User {

    /** Identifiant unique auto-incrémenté de l'utilisateur. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Adresse email unique servant d'identifiant de connexion, 255 caractères maximum. */
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /**
     * Mot de passe haché avec BCrypt.
     * Exclu de la sérialisation JSON (WRITE_ONLY) pour ne jamais l'exposer dans les réponses.
     */
    @Column(nullable = false, length = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /** Prénom de l'utilisateur, obligatoire, 100 caractères maximum. */
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    /** Nom de famille de l'utilisateur, obligatoire, 100 caractères maximum. */
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    /** Rôle de l'utilisateur, stocké sous forme de chaîne (ex : "ADMIN", "STAFF"). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role;

    /** Date et heure de création du compte, initialisée automatiquement via {@link #prePersist()}. */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public User() {
    }

    public Long getId() {
        return id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Initialise automatiquement la date de création avant le premier enregistrement en base.
     * Appelé par JPA via le cycle de vie {@code @PrePersist}.
     */
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
