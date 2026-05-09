package com.vinylove.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entité JPA représentant un événement dans l'application Vinylove.
 * Mappée sur la table {@code events} en base de données.
 * La date de création est automatiquement initialisée avant le premier enregistrement.
 */
@Entity
@Table(name = "events")
public class Event {

    /** Identifiant unique auto-incrémenté de l'événement. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom de l'événement, obligatoire, 150 caractères maximum. */
    @Column(nullable = false, length = 150)
    private String name;

    /** Description facultative de l'événement, 500 caractères maximum. */
    @Column(length = 500)
    private String description;

    /** Lieu où se déroule l'événement, obligatoire, 255 caractères maximum. */
    @Column(nullable = false, length = 255)
    private String location;

    /** Date et heure prévues de l'événement, obligatoires. */
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    /** Date et heure de création de l'enregistrement, définie automatiquement via {@link #prePersist()}. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** URL de l'image de bannière pour l'événement, 500 caractères maximum. */
    @Column(name = "banner_image_url", length = 500)
    private String bannerImageUrl;

    public Event() { 
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
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
