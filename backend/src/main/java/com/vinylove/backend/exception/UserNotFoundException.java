package com.vinylove.backend.exception;

/**
 * Exception levée lorsqu'un utilisateur recherché en base de données est introuvable
 * (par identifiant ou par email).
 * Interceptée par {@link GlobalExceptionHandler} qui retourne un HTTP 404 (Not Found).
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Crée l'exception avec un message descriptif.
     *
     * @param message message d'erreur transmis au client dans le corps de la réponse
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
