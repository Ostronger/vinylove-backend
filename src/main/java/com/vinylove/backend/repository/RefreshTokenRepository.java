package com.vinylove.backend.repository;

import com.vinylove.backend.entity.RefreshToken;
import com.vinylove.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour l'entité {@link RefreshToken}.
 * Fournit les opérations CRUD standard et des requêtes dérivées
 * pour la gestion des tokens de rafraîchissement par valeur ou par utilisateur.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Recherche un refresh token par sa valeur unique.
     * Utilisé pour la vérification lors du rafraîchissement d'access token et de la déconnexion.
     *
     * @param token valeur du token à rechercher
     * @return un {@link Optional} contenant le token trouvé, ou vide si inexistant
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Retourne tous les refresh tokens associés à un utilisateur donné.
     * Utilisé pour révoquer toutes les sessions actives (ex : après changement de mot de passe).
     *
     * @param user utilisateur dont on veut récupérer les tokens
     * @return liste des refresh tokens de l'utilisateur (actifs ou révoqués)
     */
    List<RefreshToken> findByUser(User user);
}
