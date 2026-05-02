package com.vinylove.backend.controller;

import com.vinylove.backend.dto.LoginRequest;
import com.vinylove.backend.dto.LoginResponse;
import com.vinylove.backend.entity.User;
import com.vinylove.backend.service.UserService;
import com.vinylove.backend.dto.RefreshTokenRequest;
import com.vinylove.backend.dto.RefreshTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vinylove.backend.dto.UserProfileResponse;
import org.springframework.security.core.Authentication;
import com.vinylove.backend.dto.ChangePasswordRequest;
import com.vinylove.backend.dto.LogoutRequest;

import java.util.List;

/**
 * Contrôleur REST exposant les endpoints de gestion des utilisateurs.
 * Couvre l'authentification (connexion, déconnexion, refresh de token),
 * la gestion du profil personnel et les opérations ADMIN sur les comptes.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructeur avec injection du service utilisateur.
     *
     * @param userService service métier pour la gestion des utilisateurs
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retourne la liste de tous les utilisateurs (accès ADMIN uniquement).
     *
     * @return liste d'objets {@link User}
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Retourne un utilisateur par son identifiant (accès ADMIN uniquement).
     *
     * @param id identifiant de l'utilisateur
     * @return 200 avec l'utilisateur trouvé, ou 404 si inexistant
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouveau compte utilisateur (endpoint public, utilisé pour l'inscription).
     *
     * @param user objet {@link User} reçu dans le corps de la requête
     * @return l'utilisateur persisté avec son identifiant généré
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    /**
     * Supprime un compte utilisateur par son identifiant (accès ADMIN uniquement).
     *
     * @param id identifiant de l'utilisateur à supprimer
     * @return 204 si la suppression a réussi, ou 404 si l'utilisateur est introuvable
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Authentifie un utilisateur et retourne ses tokens JWT (access + refresh).
     *
     * @param loginRequest DTO contenant l'email et le mot de passe
     * @return 200 avec un {@link LoginResponse} incluant les tokens et les informations de l'utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Retourne le profil de l'utilisateur actuellement authentifié.
     * L'email est extrait du contexte de sécurité Spring via l'objet {@link Authentication}.
     *
     * @param authentication contexte d'authentification Spring Security
     * @return 200 avec le profil de l'utilisateur sous forme de {@link UserProfileResponse}
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile(Authentication authentication) {
        String email = authentication.getName();
        UserProfileResponse response = userService.getCurrentUserProfile(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Permet à l'utilisateur connecté de changer son mot de passe.
     * Révoque tous les refresh tokens existants après le changement pour forcer une reconnexion.
     *
     * @param authentication contexte d'authentification Spring Security
     * @param request        DTO contenant le mot de passe actuel et le nouveau mot de passe
     * @return 200 avec un message de confirmation
     */
    @PutMapping("/me/password")
    public ResponseEntity<String> changeCurrentUserPassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest request
    ) {
        String email = authentication.getName();
        userService.changeCurrentUserPassword(email, request);
        return ResponseEntity.ok("Mot de passe changé avec succès");
    }

    /**
     * Génère un nouvel access token JWT à partir d'un refresh token valide.
     *
     * @param request DTO contenant le refresh token
     * @return 200 avec un {@link RefreshTokenResponse} contenant le nouvel access token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = userService.refreshAccessToken(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Déconnecte l'utilisateur en révoquant son refresh token.
     *
     * @param request DTO contenant le refresh token à révoquer
     * @return 200 avec un message de confirmation
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {
        userService.logout(request);
        return ResponseEntity.ok("Déconnexion réussie");
    }
}
