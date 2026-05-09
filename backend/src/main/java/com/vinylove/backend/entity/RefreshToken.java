package com.vinylove.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entité JPA représentant un token de rafraîchissement persisté en base de données.
 * Mappée sur la table {@code refresh_tokens}.
 * Chaque token est lié à un utilisateur et possède une date d'expiration et un état de révocation.
 * Plusieurs tokens peuvent coexister pour un même utilisateur (sessions multiples).
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    /** Identifiant unique auto-incrémenté du refresh token. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Valeur unique du token (UUID), stockée en base pour la vérification lors du rafraîchissement. */
    @Column(nullable = false, unique = true, length = 500)
    private String token;

    /** Utilisateur auquel ce token est associé ; chargé en mode lazy pour éviter les requêtes inutiles. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Date et heure d'expiration au-delà desquelles le token ne peut plus être utilisé. */
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    /** Indique si le token a été explicitement révoqué (déconnexion, changement de mot de passe). */
    @Column(nullable = false)
    private boolean revoked;

    /** Date et heure de création du token, définie automatiquement via {@link #prePersist()}. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public RefreshToken() {
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
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
