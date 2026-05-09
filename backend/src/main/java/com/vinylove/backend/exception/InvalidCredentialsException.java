package com.vinylove.backend.exception;

/**
 * Exception levée lorsque les identifiants fournis lors d'une connexion sont incorrects
 * (email introuvable ou mot de passe non correspondant).
 * Interceptée par {@link GlobalExceptionHandler} qui retourne un HTTP 401 (Unauthorized).
 */
public class InvalidCredentialsException extends RuntimeException {

    /**
     * Crée l'exception avec un message descriptif.
     *
     * @param message message d'erreur transmis au client dans le corps de la réponse
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
