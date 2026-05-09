package com.vinylove.backend.exception;

/**
 * Exception levée lorsque le mot de passe actuel fourni lors d'un changement de mot de passe
 * ne correspond pas au hash stocké en base de données.
 * Interceptée par {@link GlobalExceptionHandler} qui retourne un HTTP 400 (Bad Request).
 */
public class InvalidPasswordException extends RuntimeException {

    /**
     * Crée l'exception avec un message descriptif.
     *
     * @param message message d'erreur transmis au client dans le corps de la réponse
     */
    public InvalidPasswordException(String message) {
        super(message);
    }
}
