package com.vinylove.backend.controller;

import com.vinylove.backend.entity.Guest;
import com.vinylove.backend.service.GuestService;
import com.vinylove.backend.dto.GuestResponse;
import com.vinylove.backend.dto.CheckInRequest;
import com.vinylove.backend.service.QrCodeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Contrôleur REST exposant les endpoints de gestion des invités d'événements.
 * Les routes de lecture sont accessibles à tout utilisateur authentifié ;
 * la création, la mise à jour et la suppression sont réservées aux administrateurs (configuré dans {@code SecurityConfig}).
 */
@RestController
@RequestMapping("/api/events/{eventId}/guests")
public class GuestController {
    
    private final GuestService guestService;
    private final QrCodeService qrCodeService;

    /** Constructeur avec injection du service invité. */
    public GuestController(GuestService guestService, QrCodeService qrCodeService) {
        this.guestService = guestService;
        this.qrCodeService = qrCodeService;
    }

    /** Crée un nouvel invité pour un événement donné. */
    @PostMapping
    public GuestResponse createGuest(@PathVariable Long eventId, @RequestBody Guest guest) {
        return guestService.createGuest(eventId, guest);
    }

    /** Retourne la liste de tous les invités d'un événement donné. */
    @GetMapping
    public List<GuestResponse> getGuestsByEvent(@PathVariable Long eventId) {
        return guestService.getGuestsByEvent(eventId);
    }

    /** Retourne les détails d'un invité spécifique par son identifiant. */ 
    @GetMapping("/{guestId}")
    public ResponseEntity<GuestResponse> getGuestById(@PathVariable Long guestId) {
        return guestService.getGuestById(guestId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Supprime un invité spécifique par son identifiant. */
    @DeleteMapping("/{guestId}")
    public ResponseEntity<?> deleteGuest(@PathVariable Long guestId) {
        if (guestService.getGuestById(guestId).isPresent()) {
            guestService.deleteGuest(guestId);
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
        
    }

    /** Met à jour les informations d'un invité spécifique par son identifiant. */
    @PutMapping("/{guestId}")
    public ResponseEntity<GuestResponse> updateGuest(
            @PathVariable Long guestId, 
            @RequestBody Guest updateGuest
    ) {
        return guestService.updateGuest(guestId, updateGuest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Effectue le check-in d'un invité en utilisant son code QR unique. */
    @PostMapping("/check-in")
    public ResponseEntity<GuestResponse> checkInGuest(@RequestBody CheckInRequest request) {
        GuestResponse response = guestService.checkInGuest(request);
        return ResponseEntity.ok(response);
    }

    /** Génère et retourne le code QR d'un invité spécifique par son identifiant. */
    @GetMapping(value = "/{guestId}/qr-code", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getGuestQrCode(@PathVariable Long guestId) {
        GuestResponse guest = guestService.getGuestById(guestId)
                .orElseThrow(() -> new RuntimeException("Invité introuvable"));

        String qrContent = "http://localhost:8080/api/check-in?code=" + guest.getQrCode();
        
        byte[] qrCodeImage = qrCodeService.generateQrCode(qrContent, 300, 300);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCodeImage);
    }
}
