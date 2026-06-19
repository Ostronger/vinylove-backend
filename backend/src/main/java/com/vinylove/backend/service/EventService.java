package com.vinylove.backend.service;

import com.vinylove.backend.entity.Event;
import com.vinylove.backend.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des événements.
 * Fait le lien entre {@link com.vinylove.backend.controller.EventController}
 * et {@link EventRepository} pour les opérations CRUD sur les événements.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;

    /**
     * Constructeur avec injection du repository événement.
     *
     * @param eventRepository repository JPA pour l'accès aux données événement
     */
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Persiste un nouvel événement en base de données.
     *
     * @param event objet {@link Event} à enregistrer
     * @return l'événement persisté avec son identifiant généré et sa date de création initialisée
     */
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    /**
     * Retourne la liste de tous les événements enregistrés en base.
     *
     * @return liste d'objets {@link Event}, vide si aucun événement n'existe
     */
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * Recherche un événement par son identifiant.
     *
     * @param id identifiant de l'événement à rechercher
     * @return un {@link Optional} contenant l'événement trouvé, ou vide si inexistant
     */
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    /**
     * Supprime un événement de la base de données par son identifiant.
     *
     * @param id identifiant de l'événement à supprimer
     */
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    /**
     * Met à jour un événement existant avec de nouvelles données.
     *
     * @param id           identifiant de l'événement à mettre à jour
     * @param updatedEvent objet {@link Event} contenant les nouvelles données
     * @return un {@link Optional} contenant l'événement mis à jour, ou vide si l'événement d'origine n'existe pas
     */
    public Optional<Event> updateEvent(Long id, Event updatedEvent) {
        return eventRepository.findById(id).map(event -> {
            event.setName(updatedEvent.getName());
            event.setDescription(updatedEvent.getDescription());
            event.setLocation(updatedEvent.getLocation());
            event.setEventDate(updatedEvent.getEventDate());
            event.setBannerImageUrl(updatedEvent.getBannerImageUrl());
            return eventRepository.save(event);
        });
    }

    public Optional<Event> activateEvent(Long id) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setActive(true);
                    return eventRepository.save(event);
                });
    }

    public Optional<Event> deactivateEvent(Long id) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setActive(false);
                    return eventRepository.save(event);
                    });
    }

    public List<Event> getActiveEvents() {
        return eventRepository.findAll()
                .stream()
                .filter(Event::isActive)
                .toList();
    }


}
