package com.vinylove.backend.dto;

/**
 * DTO représentant la réponse renvoyée au client pour une table d'invitation.
 * Expose les données publiques de la table d'invitation sans retourner directement l'entité JPA,
 * ce qui permet de contrôler précisément les champs sérialisés (notamment l'id de l'événement
 * au lieu de l'objet {@link com.vinylove.backend.entity.Event} complet).
 */

public class InvitationTableResponse {

    private Long id;

    private String label;

    private String guestText;

    private int capacity;

    private int scanCount;

    private String qrCode;

    private Long eventId;

    private String eventName;

    public InvitationTableResponse() {
    }

    public InvitationTableResponse(
            Long id,
            String label,
            String guestText,
            int capacity,
            int scanCount,
            String qrCode,
            Long eventId,
            String eventName
    ) {
        this.id = id;
        this.label = label;
        this.guestText = guestText;
        this.capacity = capacity;
        this.scanCount = scanCount;
        this.qrCode = qrCode;
        this.eventId = eventId;
        this.eventName = eventName;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getGuestText() {
        return guestText;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getScanCount() {
        return scanCount;
    }

    public String getQrCode() {
        return qrCode;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }
}
