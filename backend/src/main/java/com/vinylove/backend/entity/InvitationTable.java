package com.vinylove.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invitation_tables")  
public class InvitationTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String label;

    @Column(name = "guest_text", nullable = false, columnDefinition = "TEXT")
    private String guestText;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "scan_count", nullable = false)
    private Integer scanCount = 0;

    @Column(name = "qr_code", nullable = false, unique = true, length = 100)
    private String qrCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public InvitationTable() {
        // Constructeur par défaut requis par JPA
    }

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.qrCode == null || this.qrCode.isEmpty()) {
            this.qrCode = UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getGuestText() {
        return guestText;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getScanCount() {
        return scanCount;
    }

    public String getQrCode() {
        return qrCode;
    }

    public Event getEvent() {
        return event;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public void setGuestText(String guestText) {
        this.guestText = guestText;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setScanCount(Integer scanCount) {
        this.scanCount = scanCount;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}
