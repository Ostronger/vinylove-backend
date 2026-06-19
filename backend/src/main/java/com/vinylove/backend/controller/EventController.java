package com.vinylove.backend.controller;

import com.vinylove.backend.entity.Event;
import com.vinylove.backend.service.EventService;
import com.vinylove.backend.dto.EventStatsResponse;
import com.vinylove.backend.dto.InvitationTableStatsResponse;
import com.vinylove.backend.service.AdminStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST exposant les endpoints de gestion des événements.
 * Les routes de lecture sont accessibles à tout utilisateur authentifié ;
 * la création et la suppression sont réservées aux administrateurs (configuré dans {@code SecurityConfig}).
 */
@RestController
@RequestMapping("/api/events") 
public class EventController {

    private final EventService eventService;
    private final AdminStatsService adminStatsService;

    /**
     * Constructeur avec injection du service événement.
     *
     * @param eventService service métier pour la gestion des événements
     * @param adminStatsService service pour les statistiques administratives
     */
    public EventController(EventService eventService, AdminStatsService adminStatsService) {
        this.eventService = eventService;
        this.adminStatsService = adminStatsService;
    }

    /**
     * Crée un nouvel événement en base de données.
     *
     * @param event objet {@link Event} reçu dans le corps de la requête
     * @return l'événement persisté avec son identifiant généré
     */
    @PostMapping //(" POST /api/events")
    public Event createEvent(@RequestBody Event event) {
        return eventService.saveEvent(event);
    }

    /**
     * Retourne la liste de tous les événements enregistrés.
     *
     * @return liste d'objets {@link Event}
     */
    @GetMapping //(" GET /api/events")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    /**
     * Retourne la liste des événements actifs.
     *
     * @return liste d'objets {@link Event}
     */
    @GetMapping("/active")
    public List<Event> getActiveEvents() {
        return eventService.getActiveEvents();
    }

    @GetMapping("/{eventId}/stats")
    public EventStatsResponse getStatsForEvent(@PathVariable Long eventId) {
        return adminStatsService.getStatsForEvent(eventId);
    }

    @GetMapping("/{eventId}/tables/stats")
    public List<InvitationTableStatsResponse> getTableStatsForEvent(@PathVariable Long eventId) {
        return adminStatsService.getTableStatsForEvent(eventId);
    }

    /**
     * Retourne un événement par son identifiant.
     *
     * @param id identifiant de l'événement
     * @return 200 avec l'événement trouvé, ou 404 si inexistant
     */
    @GetMapping("/{id}") //(" GET /api/events/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un événement par son identifiant.
     *
     * @param id identifiant de l'événement à supprimer
     * @return 204 si la suppression a réussi, ou 404 si l'événement est introuvable
     */
    @DeleteMapping("/{id}") //(" DELETE /api/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (eventService.getEventById(id).isPresent()) {
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Met à jour un événement existant par son identifiant.
     *
     * @param id identifiant de l'événement à mettre à jour
     * @param updatedEvent objet {@link Event} contenant les nouvelles données
     * @return 200 avec l'événement mis à jour, ou 404 si l'événement est introuvable
     */
    @PutMapping("/{id}") //(" PUT /api/events/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
        return eventService.updateEvent(id, updatedEvent)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Active un événement existant par son identifiant.
     *
     * @param id identifiant de l'événement à activer
     * @return 200 avec l'événement activé, ou 404 si l'événement est introuvable
     */
    @PutMapping("/{id}/activate") //(" PUT /api/events/{id}/activate")
    public ResponseEntity<Event> activateEvent(@PathVariable Long id) {
        return eventService.activateEvent(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Désactive un événement existant par son identifiant.
     *
     * @param id identifiant de l'événement à désactiver
     * @return 200 avec l'événement désactivé, ou 404 si l'événement est introuvable
     */
    @PutMapping("/{id}/deactivate") //(" PUT /api/events/{id}/deactivate")
    public ResponseEntity<Event> deactivateEvent(@PathVariable Long id) {
        return eventService.deactivateEvent(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
