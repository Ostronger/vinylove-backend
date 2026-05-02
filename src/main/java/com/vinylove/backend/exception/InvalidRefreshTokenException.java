package com.vinylove.backend.exception;

/**
 * Exception levée lorsqu'un refresh token est invalide, introuvable en base,
 * explicitement révoqué, ou arrivé à expiration.
 * Interceptée par {@link GlobalExceptionHandler} qui retourne un HTTP 401 (Unauthorized).
 */
public class InvalidRefreshTokenException extends RuntimeException {

    /**
     * Crée l'exception avec un message descriptif.
     *
     * @param message message d'erreur transmis au client dans le corps de la réponse
     */
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
