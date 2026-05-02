package com.vinylove.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité JPA représentant un invité (Guest) associé à un événement dans l'application Vinylove.
 * Mappée sur la table {@code guests} en base de données.
 * Chaque invité appartient à un seul événement et peut être marqué comme présent (check-in).
 * La date de création est automatiquement initialisée avant le premier enregistrement.
 */
@Entity
@Table(name = "guests")
public class Guest {

    /** Identifiant unique auto-incrémenté de l'invité. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Prénom de l'invité, obligatoire, 100 caractères maximum. */
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    /** Nom de famille de l'invité, obligatoire, 100 caractères maximum. */
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    /** Adresse email de l'invité, obligatoire, 100 caractères maximum. */
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    /** Numéro de téléphone de l'invité, facultatif, 20 caractères maximum. */
    @Column(name = "phone", nullable = true, length = 20)
    private String phone;

    /** Événement auquel l'invité est rattaché ; chargé en mode lazy pour éviter les requêtes inutiles. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    /** Indique si l'invité s'est présenté à l'événement (check-in effectué). Faux par défaut. */
    @Column(name = "checked_in", nullable = false)
    private boolean checkedIn = false;

    /** Date et heure de création de l'enregistrement, définie automatiquement via {@link #prePersist()}. */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** Date et heure de la dernière mise à jour de l'enregistrement, mise à jour automatiquement via {@link #preUpdate()}. */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Code QR unique généré pour chaque invité, utilisé pour le check-in. */
    @Column(name = "qr_code", nullable = false, unique = true, length = 100)
    private String qrCode;

    /** Date et heure du check-in de l'invité, définie lors du processus de check-in. */
    @Column (name = "checked_in_at")
    private LocalDateTime checkedInAt;

    public Guest() {
    }

    public Long getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(LocalDateTime checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    /**
     * Initialise automatiquement la date de création avant le premier enregistrement en base.
     * Appelé par JPA via le cycle de vie {@code @PrePersist}.
     */
    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.qrCode == null || this.qrCode.isBlank()) {
            this.qrCode = UUID.randomUUID().toString();
        }
    }

    /**
     * Met à jour automatiquement la date de mise à jour avant chaque modification en base.
     * Appelé par JPA via le cycle de vie {@code @PreUpdate}.
     */
    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
