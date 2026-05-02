package com.vinylove.backend.entity;

/**
 * Enumération des rôles disponibles dans l'application Vinylove.
 * Utilisée pour le contrôle d'accès basé sur les rôles (RBAC) via Spring Security.
 * <ul>
 *   <li>{@code ADMIN} — accès complet à toutes les ressources et opérations</li>
 *   <li>{@code STAFF} — accès en lecture aux événements et gestion du profil personnel</li>
 * </ul>
 */
public enum Role {
    ADMIN,
    STAFF
}
