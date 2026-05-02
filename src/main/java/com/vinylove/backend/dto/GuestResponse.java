package com.vinylove.backend.dto;

import java.time.LocalDateTime;

/**
 * DTO représentant la réponse renvoyée au client pour un invité.
 * Expose les données publiques de l'invité sans retourner directement l'entité JPA,
 * ce qui permet de contrôler précisément les champs sérialisés (notamment l'id de l'événement
 * au lieu de l'objet {@link com.vinylove.backend.entity.Event} complet).
 */
public class GuestResponse {

    /** Identifiant unique de l'invité. */
    private Long id;

    /** Prénom de l'invité. */
    private String firstName;

    /** Nom de famille de l'invité. */
    private String lastName;

    /** Adresse email de l'invité. */
    private String email;

    /** Numéro de téléphone de l'invité (facultatif). */
    private String phone;

    /** Indique si l'invité a effectué son check-in à l'événement. */
    private boolean checkedIn;

    /** Identifiant de l'événement auquel l'invité est rattaché. */
    private Long eventId;

    /** Date et heure de création de l'enregistrement. */
    private LocalDateTime createdAt;

    /** Date et heure de la dernière mise à jour de l'enregistrement. */
    private LocalDateTime updatedAt;

    /** Code QR unique généré pour chaque invité, utilisé pour le check-in. */
    private String qrCode;

    public GuestResponse() {
    }

    /**
     * Constructeur complet utilisé lors du mapping depuis l'entité {@link com.vinylove.backend.entity.Guest}.
     *
     * @param id        identifiant de l'invité
     * @param firstName prénom de l'invité
     * @param lastName  nom de famille de l'invité
     * @param email     adresse email de l'invité
     * @param phone     numéro de téléphone de l'invité
     * @param checkedIn statut de présence de l'invité
     * @param eventId   identifiant de l'événement associé
     * @param createdAt date de création de l'enregistrement
     * @param updatedAt date de dernière mise à jour de l'enregistrement
     * @param qrCode    code QR de l'invité
     */
    public GuestResponse(Long id, String firstName, String lastName, String email, String phone,
                         boolean checkedIn, Long eventId, LocalDateTime createdAt, LocalDateTime updatedAt, String qrCode) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.checkedIn = checkedIn;
        this.eventId = eventId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.qrCode = qrCode;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public boolean isCheckedIn() { return checkedIn; }
    public Long getEventId() { return eventId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getQrCode() { return qrCode; }
    public void setId(Long id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setCheckedIn(boolean checkedIn) { this.checkedIn = checkedIn; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
}
