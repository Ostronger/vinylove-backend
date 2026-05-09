package com.vinylove.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Gestionnaire global des exceptions pour tous les contrôleurs REST de l'application.
 * Centralise la transformation des exceptions métier en réponses HTTP standardisées,
 * évitant la duplication de la gestion d'erreurs dans chaque contrôleur.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les tentatives d'inscription ou de mise à jour avec un email déjà utilisé.
     *
     * @param ex exception contenant le message d'erreur
     * @return réponse HTTP 409 (Conflict) avec le message d'erreur
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Gère les tentatives de connexion avec des identifiants incorrects.
     *
     * @param ex exception contenant le message d'erreur
     * @return réponse HTTP 401 (Unauthorized) avec le message d'erreur
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * Gère les accès à un utilisateur inexistant en base de données.
     *
     * @param ex exception contenant le message d'erreur
     * @return réponse HTTP 404 (Not Found) avec le message d'erreur
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Gère les tentatives de changement de mot de passe avec un mot de passe actuel incorrect.
     *
     * @param ex exception contenant le message d'erreur
     * @return réponse HTTP 400 (Bad Request) avec le message d'erreur
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Gère les tentatives de rafraîchissement ou de déconnexion avec un refresh token invalide,
     * révoqué ou expiré.
     *
     * @param ex exception contenant le message d'erreur
     * @return réponse HTTP 401 (Unauthorized) avec le message d'erreur
     */
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<String> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * Gère les tentatives d'accès à un événement inexistant ou les opérations invalides sur les invités.
     *
     * @param ex exception contenant le message d'erreur
     * @return réponse HTTP 404 (Not Found) ou 400 (Bad Request) selon le type d'erreur
     */
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<String> handleEventNotFoundException(EventNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Gère les tentatives d'ajout ou de suppression d'invités invalides (ex : email manquant, invité déjà ajouté, etc.).
     *
     * @param ex exception contenant le message d'erreur
     * @return réponse HTTP 400 (Bad Request) avec le message d'erreur
     */
    @ExceptionHandler(InvalidGuestException.class)
    public ResponseEntity<String> handleInvalidGuestException(InvalidGuestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
    * Gère les tentatives d'ajout d'un invité déjà présent dans un événement.
    *
    * @param ex exception contenant le message d'erreur
    * @return réponse HTTP 409 (Conflict) avec le message d'erreur
    */
    @ExceptionHandler(GuestAlreadyExistsException.class)
    public ResponseEntity<String> handleGuestAlreadyExistsException(GuestAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    
    }

    /**
     * Gère les tentatives d'accès ou de modification d'un invité inexistant dans un événement.
     *
     * @param ex exception contenant le message d'erreur
     * @return réponse HTTP 404 (Not Found) avec le message d'erreur
     */
    @ExceptionHandler(GuestNotFoundException.class)
    public ResponseEntity<String> handleGuestNotFoundException(GuestNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
