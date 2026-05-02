package com.vinylove.backend.service;

import com.vinylove.backend.entity.RefreshToken;
import com.vinylove.backend.entity.User;
import com.vinylove.backend.exception.InvalidRefreshTokenException;
import com.vinylove.backend.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service métier pour la gestion du cycle de vie des refresh tokens.
 * Gère la création, la vérification et la révocation des tokens de rafraîchissement
 * persistés en base de données, utilisés pour renouveler les access tokens JWT.
 */
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Constructeur avec injection du repository refresh token.
     *
     * @param refreshTokenRepository repository JPA pour l'accès aux données des refresh tokens
     */
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Crée et persiste un nouveau refresh token pour l'utilisateur donné.
     * Le token est un UUID aléatoire valide 7 jours à partir de la création.
     *
     * @param user utilisateur auquel associer le nouveau refresh token
     * @return le refresh token persisté avec sa date d'expiration et son état non révoqué
     */
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        // UUID garantit l'unicité du token sans prédictibilité
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Vérifie la validité d'un refresh token en base de données.
     * Contrôle successivement : existence, non-révocation, non-expiration.
     *
     * @param token valeur du refresh token à vérifier
     * @return le refresh token valide trouvé en base
     * @throws InvalidRefreshTokenException si le token est introuvable, révoqué ou expiré
     */
    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token invalide"));

        if (refreshToken.isRevoked()) {
            throw new InvalidRefreshTokenException("Refresh token révoqué");
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidRefreshTokenException("Refresh token expiré");
        }

        return refreshToken;
    }

    /**
     * Révoque un refresh token spécifique, l'invalidant pour toute utilisation future.
     * Utilisé lors d'une déconnexion explicite.
     *
     * @param token valeur du refresh token à révoquer
     * @throws InvalidRefreshTokenException si le token est introuvable en base
     */
    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token invalide"));

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    /**
     * Révoque tous les refresh tokens actifs d'un utilisateur en une seule opération batch.
     * Utilisé après un changement de mot de passe pour forcer la reconnexion sur tous les appareils.
     *
     * @param user utilisateur dont tous les refresh tokens doivent être révoqués
     */
    public void revokeAllUserRefreshTokens(User user) {
        List<RefreshToken> refreshTokens = refreshTokenRepository.findByUser(user);

        for (RefreshToken refreshToken : refreshTokens) {
            refreshToken.setRevoked(true);
        }

        // Sauvegarde en batch pour minimiser le nombre de requêtes SQL
        refreshTokenRepository.saveAll(refreshTokens);
    }
}
