package com.vinylove.backend.exception;

/**
 * Exception levée lorsqu'un événement recherché en base de données est introuvable.
 * Interceptée par {@link GlobalExceptionHandler} qui retourne un HTTP 404 (Not Found).
 */
public class EventNotFoundException extends RuntimeException {

    /**
     * Crée l'exception avec un message descriptif.
     *
     * @param message message d'erreur transmis au client dans le corps de la réponse
     */
    public EventNotFoundException(String message) {
        super(message);
    }
}
