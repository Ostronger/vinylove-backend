package com.vinylove.backend.exception;

/**
 * Exception levée lorsqu'une tentative d'inscription ou de mise à jour de profil
 * utilise une adresse email déjà enregistrée en base de données.
 * Interceptée par {@link GlobalExceptionHandler} qui retourne un HTTP 409 (Conflict).
 */
public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Crée l'exception avec un message descriptif.
     *
     * @param message message d'erreur transmis au client dans le corps de la réponse
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
