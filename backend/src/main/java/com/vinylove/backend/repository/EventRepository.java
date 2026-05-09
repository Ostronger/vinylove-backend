package com.vinylove.backend.repository;

import com.vinylove.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository JPA pour l'entité {@link Event}.
 * Fournit les opérations CRUD standard héritées de {@link JpaRepository}
 * (findAll, findById, save, deleteById, etc.) sans implémentation supplémentaire.
 */
public interface EventRepository extends JpaRepository<Event, Long> {
}
