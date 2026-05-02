package com.vinylove.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.vinylove.backend.entity.Event;
import com.vinylove.backend.entity.Guest;
import com.vinylove.backend.exception.EventNotFoundException;
import com.vinylove.backend.exception.GuestAlreadyExistsException;
import com.vinylove.backend.exception.InvalidGuestException;
import com.vinylove.backend.exception.GuestNotFoundException;
import com.vinylove.backend.repository.GuestRepository;
import com.vinylove.backend.dto.GuestResponse;
import com.vinylove.backend.dto.CheckInRequest;
import com.vinylove.backend.dto.CheckInResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion des invités d'un événement.
 * Orchestre les validations métier (unicité d'email, présence d'un moyen de contact),
 * le rattachement à l'événement parent et la conversion vers le DTO {@link GuestResponse}.
 */
@Service
public class GuestService {

    private final GuestRepository guestRepository;
    private final EventService eventService;

    /**
     * Convertit une entité {@link Guest} en DTO {@link GuestResponse}.
     * Expose l'identifiant de l'événement plutôt que l'objet complet pour éviter
     * la sérialisation récursive lors du chargement lazy de la relation.
     *
     * @param guest entité invité à convertir
     * @return DTO {@link GuestResponse} prêt à être sérialisé en JSON
     */
    private GuestResponse mapToGuestResponse(Guest guest) {
        return new GuestResponse(
                guest.getId(),
                guest.getFirstName(),
                guest.getLastName(),
                guest.getEmail(),
                guest.getPhone(),
                guest.isCheckedIn(),
                guest.getEvent().getId(),
                guest.getCreatedAt(),
                guest.getUpdatedAt(),
                guest.getQrCode(),
                guest.getCheckedInAt()
        );
    }

    /**
     * Constructeur avec injection des dépendances nécessaires.
     *
     * @param guestRepository repository JPA pour l'accès aux données des invités
     * @param eventService    service événement pour la résolution de l'événement parent
     */
    public GuestService(GuestRepository guestRepository, EventService eventService) {
        this.guestRepository = guestRepository;
        this.eventService = eventService;
    }

    /**
     * Crée et persiste un nouvel invité pour l'événement spécifié.
     * Applique les règles de validation : au moins un moyen de contact (email ou téléphone)
     * doit être fourni, et l'email doit être unique au sein de l'événement.
     *
     * @param eventId identifiant de l'événement auquel rattacher l'invité
     * @param guest   objet {@link Guest} contenant les données de l'invité à créer
     * @return {@link GuestResponse} de l'invité créé
     * @throws EventNotFoundException      si l'événement est introuvable
     * @throws InvalidGuestException       si ni email ni téléphone n'est fourni
     * @throws GuestAlreadyExistsException si un invité avec le même email existe déjà pour cet événement
     */
    public GuestResponse createGuest(Long eventId, Guest guest) {
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Événement introuvable"));

        // Au moins un moyen de contact est obligatoire pour identifier l'invité
        if ((guest.getEmail() == null || guest.getEmail().isBlank())
                && (guest.getPhone() == null || guest.getPhone().isBlank())) {
            throw new InvalidGuestException("Un email ou un téléphone est obligatoire");
        }

        // Vérification de l'unicité de l'email au sein de l'événement uniquement si un email est fourni
        if (guest.getEmail() != null && !guest.getEmail().isBlank()
                && guestRepository.existsByEventAndEmail(event, guest.getEmail())) {
            throw new GuestAlreadyExistsException("Un invité avec cet email existe déjà pour cet événement");
        }

        guest.setEvent(event);
        // Le check-in est toujours initialisé à false à la création
        guest.setCheckedIn(false);

        Guest savedGuest = guestRepository.save(guest);
        return mapToGuestResponse(savedGuest);
    }

    /**
     * Retourne la liste de tous les invités d'un événement donné.
     *
     * @param eventId identifiant de l'événement
     * @return liste de {@link GuestResponse} pour chaque invité de l'événement
     * @throws EventNotFoundException si l'événement est introuvable
     */
    public List<GuestResponse> getGuestsByEvent(Long eventId) {
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Événement introuvable"));

        return guestRepository.findByEvent(event)
                .stream()
                .map(this::mapToGuestResponse)
                .collect(Collectors.toList());
    }

    /**
     * Recherche un invité par son identifiant et retourne son DTO.
     *
     * @param id identifiant de l'invité à rechercher
     * @return un {@link Optional} contenant le {@link GuestResponse} trouvé, ou vide si inexistant
     */
    public Optional<GuestResponse> getGuestById(Long id) {
        return guestRepository.findById(id)
            .map(this::mapToGuestResponse);
    }

    /**
     * Supprime un invité de la base de données par son identifiant.
     *
     * @param id identifiant de l'invité à supprimer
     */
    public void deleteGuest(Long id) {
        guestRepository.deleteById(id);
    }

    /**
     * Met à jour les informations d'un invité existant.
     * Ré-applique la règle de validation sur la présence d'un moyen de contact.
     * Le statut de check-in et le rattachement à l'événement ne sont pas modifiés par cette méthode.
     *
     * @param guestId     identifiant de l'invité à mettre à jour
     * @param updateGuest objet {@link Guest} contenant les nouvelles données (prénom, nom, email, téléphone)
     * @return un {@link Optional} contenant le {@link GuestResponse} mis à jour, ou vide si l'invité est inexistant
     * @throws InvalidGuestException si ni email ni téléphone n'est fourni dans les nouvelles données
     */
    public Optional<GuestResponse> updateGuest(Long guestId, Guest updateGuest) {
        return guestRepository.findById(guestId).map(guest -> {

            // Revalide la contrainte de contact lors de la mise à jour
            if ((updateGuest.getEmail() == null || updateGuest.getEmail().isBlank())
                    && (updateGuest.getPhone() == null || updateGuest.getPhone().isBlank())) {
                throw new InvalidGuestException("Un email ou un téléphone est obligatoire");
            }

            guest.setFirstName(updateGuest.getFirstName());
            guest.setLastName(updateGuest.getLastName());
            guest.setEmail(updateGuest.getEmail());
            guest.setPhone(updateGuest.getPhone());

            Guest savedGuest = guestRepository.save(guest);
            return mapToGuestResponse(savedGuest);
        });
    }

    /**
     * Effectue le check-in d'un invité en utilisant son code QR unique.
     * Vérifie que l'invité existe et n'a pas déjà effectué son check-in avant de mettre à jour son statut.
     *
     * @param request objet {@link CheckInRequest} contenant le code QR de l'invité à enregistrer
     * @return {@link GuestResponse} de l'invité après mise à jour du statut de check-in
     * @throws GuestNotFoundException   si aucun invité ne correspond au code QR fourni
     * @throws InvalidGuestException    si l'invité a déjà effectué son check-in
     */
    public GuestResponse checkInGuest(CheckInRequest request) {
        Guest guest = guestRepository.findByQrCode(request.getQrCode())
                .orElseThrow(() -> new GuestNotFoundException("Invité introuvable"));

        if (guest.isCheckedIn()) {
            throw new InvalidGuestException("Invité déjà enregistré");
        }

        guest.setCheckedIn(true);
        guest.setCheckedInAt(java.time.LocalDateTime.now());
        
        Guest savedGuest = guestRepository.save(guest);
        
        return mapToGuestResponse(savedGuest);
    }

    @Transactional
    public CheckInResponse checkInByQrCode(String qrCode) {
        Guest guest = guestRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new GuestNotFoundException("Invité introuvable"));

        if (guest.isCheckedIn()) {
            throw new InvalidGuestException("Invité déjà enregistré");
        }

        guest.setCheckedIn(true);
        guest.setCheckedInAt(LocalDateTime.now());

        Guest savedGuest = guestRepository.save(guest);

        return new CheckInResponse(
                "Entrée validée",
                savedGuest.getId(),
                savedGuest.getFirstName() + " " + savedGuest.getLastName(),
                savedGuest.getEvent().getId(),
                savedGuest.getEvent().getName(),
                savedGuest.isCheckedIn(),
                savedGuest.getCheckedInAt()
        );
    }
}
