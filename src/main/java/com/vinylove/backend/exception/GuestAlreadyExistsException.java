package com.vinylove.backend.exception;

/**
 * Exception levée lorsqu'un invité avec le même email est déjà enregistré
 * pour le même événement, empêchant ainsi les doublons au sein d'un même événement.
 * Interceptée par {@link GlobalExceptionHandler} qui retourne un HTTP 409 (Conflict).
 */
public class GuestAlreadyExistsException extends RuntimeException {

    /**
     * Crée l'exception avec un message descriptif.
     *
     * @param message message d'erreur transmis au client dans le corps de la réponse
     */
    public GuestAlreadyExistsException(String message) {
        super(message);
    }
}
