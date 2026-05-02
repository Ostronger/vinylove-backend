package com.vinylove.backend.exception;

/**
 * Exception levée lorsque les données d'un invité ne respectent pas les règles de validation métier.
 * Par exemple : absence à la fois d'email et de numéro de téléphone, qui sont mutuellement obligatoires.
 * Interceptée par {@link GlobalExceptionHandler} qui retourne un HTTP 400 (Bad Request).
 */
public class InvalidGuestException extends RuntimeException {

    /**
     * Crée l'exception avec un message descriptif.
     *
     * @param message message d'erreur transmis au client dans le corps de la réponse
     */
    public InvalidGuestException(String message) {
        super(message);
    }
}
