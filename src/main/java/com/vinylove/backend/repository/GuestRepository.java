package com.vinylove.backend.repository;

import com.vinylove.backend.entity.Event;
import com.vinylove.backend.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour l'entité {@link Guest}.
 * Fournit les opérations CRUD standard héritées de {@link JpaRepository}
 * ainsi que des requêtes dérivées pour la gestion des invités par événement
 * et la vérification de doublons par email au sein d'un même événement.
 */
public interface GuestRepository extends JpaRepository<Guest, Long> {

    /**
     * Retourne tous les invités rattachés à un événement donné.
     *
     * @param event événement dont on veut récupérer la liste des invités
     * @return liste des invités de l'événement, vide si aucun invité n'est enregistré
     */
    List<Guest> findByEvent(Event event);

    /**
     * Vérifie si un invité avec un email donné existe déjà pour un événement spécifique.
     * Utilisé pour prévenir les doublons lors de la création d'un invité.
     *
     * @param event événement dans lequel vérifier l'existence de l'email
     * @param email adresse email à vérifier
     * @return {@code true} si un invité avec cet email existe déjà pour cet événement, {@code false} sinon
     */
    boolean existsByEventAndEmail(Event event, String email);

    /**
     * Recherche un invité par son code QR unique.
     * Utilisé pour le processus de check-in via le code QR.
     *
     * @param qrCode code QR de l'invité à rechercher
     * @return {@link Optional} contenant l'invité trouvé, ou vide si aucun invité ne correspond au code QR
     */
    Optional<Guest> findByQrCode(String qrCode);
}
