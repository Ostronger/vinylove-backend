package com.vinylove.backend.repository;

import com.vinylove.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository JPA pour l'entité {@link User}.
 * Fournit les opérations CRUD standard et une requête dérivée
 * pour la recherche d'utilisateur par email, utilisée lors de l'authentification.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recherche un utilisateur par son adresse email.
     * Utilisé lors de la connexion, du chargement des détails Spring Security
     * et de la vérification d'unicité d'email.
     *
     * @param email adresse email à rechercher
     * @return un {@link Optional} contenant l'utilisateur trouvé, ou vide si inexistant
     */
    Optional<User> findByEmail(String email);
}
