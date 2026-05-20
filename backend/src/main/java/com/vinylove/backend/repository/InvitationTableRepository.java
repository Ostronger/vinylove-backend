package com.vinylove.backend.repository;

import com.vinylove.backend.entity.Event;
import com.vinylove.backend.entity.InvitationTable;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository JPA pour l'entité {@link InvitationTable}.
 * Fournit les opérations CRUD standard héritées de {@link JpaRepository}
 * ainsi que des méthodes personnalisées pour la gestion des tables d'invitation.
 */
public interface InvitationTableRepository extends JpaRepository<InvitationTable, Long> {

    /**
     * Retourne le nombre total de tables d'invitation associées à un événement donné.
     *
     * @param event événement dont on veut compter les tables d'invitation
     * @return nombre total de tables d'invitation pour l'événement
     */
    long countByEvent(Event event);

    List<InvitationTable> findByEvent(Event event);

    Optional<InvitationTable> findByQrCode(String qrCode);
    
}
