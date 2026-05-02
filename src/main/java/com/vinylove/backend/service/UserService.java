package com.vinylove.backend.service;

import com.vinylove.backend.entity.User;
import com.vinylove.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vinylove.backend.exception.EmailAlreadyExistsException;
import com.vinylove.backend.dto.LoginRequest;
import com.vinylove.backend.dto.LoginResponse;
import com.vinylove.backend.dto.RefreshTokenRequest;
import com.vinylove.backend.dto.RefreshTokenResponse;
import com.vinylove.backend.dto.UpdateUserProfileRequest;
import com.vinylove.backend.exception.InvalidCredentialsException;
import com.vinylove.backend.dto.UserProfileResponse;
import com.vinylove.backend.exception.UserNotFoundException;
import com.vinylove.backend.dto.ChangePasswordRequest;
import com.vinylove.backend.exception.InvalidPasswordException;
import com.vinylove.backend.entity.RefreshToken;
import com.vinylove.backend.dto.LogoutRequest;

import java.util.List;
import java.util.Optional;

/**
 * Service métier centralisant toutes les opérations liées aux utilisateurs :
 * inscription, authentification, gestion du profil, changement de mot de passe,
 * rafraîchissement de token et déconnexion.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    /**
     * Constructeur avec injection de toutes les dépendances nécessaires.
     *
     * @param userRepository       repository JPA pour l'accès aux données utilisateur
     * @param passwordEncoder      encodeur BCrypt pour le hachage des mots de passe
     * @param jwtService           service de génération et validation des tokens JWT
     * @param refreshTokenService  service de gestion des refresh tokens
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Inscrit un nouvel utilisateur après vérification de l'unicité de l'email
     * et hachage du mot de passe en clair.
     *
     * @param user objet {@link User} contenant les informations de l'utilisateur à créer
     * @return l'utilisateur persisté avec son identifiant généré
     * @throws EmailAlreadyExistsException si l'email est déjà enregistré en base
     */
    public User saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email déjà utilisé");
        }
        // Le mot de passe est haché avant persistance ; jamais stocké en clair
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Retourne la liste de tous les utilisateurs enregistrés en base.
     *
     * @return liste d'objets {@link User}
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id identifiant de l'utilisateur
     * @return un {@link Optional} contenant l'utilisateur trouvé, ou vide si inexistant
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Recherche un utilisateur par son adresse email.
     *
     * @param email adresse email de l'utilisateur
     * @return un {@link Optional} contenant l'utilisateur trouvé, ou vide si inexistant
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Supprime un utilisateur de la base de données par son identifiant.
     *
     * @param id identifiant de l'utilisateur à supprimer
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Authentifie un utilisateur et retourne ses informations avec les tokens JWT.
     * Vérifie successivement l'existence de l'email puis la correspondance du mot de passe.
     *
     * @param loginRequest DTO contenant l'email et le mot de passe en clair
     * @return {@link LoginResponse} avec les données publiques de l'utilisateur, l'access token et le refresh token
     * @throws InvalidCredentialsException si l'email est introuvable ou le mot de passe incorrect
     */
    public LoginResponse login(LoginRequest loginRequest) {
        // Étape 1 : cherche l'utilisateur par email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Email ou mot de passe incorrect"));

        // Étape 2 : compare le mot de passe fourni avec le hash BCrypt stocké
        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new InvalidCredentialsException("Email ou mot de passe incorrect");
        }

        String accessToken = jwtService.generateToken(user.getEmail(), user.getRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                "Connexion réussie",
                accessToken,
                refreshToken.getToken()
        );
    }

    /**
     * Retourne le profil public de l'utilisateur actuellement authentifié.
     *
     * @param email adresse email extraite du contexte d'authentification Spring Security
     * @return {@link UserProfileResponse} avec les données publiques de l'utilisateur
     * @throws UserNotFoundException si l'email ne correspond à aucun utilisateur en base
     */
    public UserProfileResponse getCurrentUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));

        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }

    /**
     * Met à jour les informations de profil de l'utilisateur connecté.
     * Vérifie que le nouvel email n'est pas déjà utilisé par un autre compte.
     *
     * @param currentEmail email actuel de l'utilisateur, extrait du contexte d'authentification
     * @param request      DTO contenant les nouvelles valeurs (email, prénom, nom)
     * @return {@link UserProfileResponse} avec les données du profil mis à jour
     * @throws UserNotFoundException       si l'utilisateur est introuvable
     * @throws EmailAlreadyExistsException si le nouvel email appartient déjà à un autre compte
     */
    public UserProfileResponse UpdateCurrentUserProfile(String currentEmail, UpdateUserProfileRequest request) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));

        // Vérifie l'unicité du nouvel email uniquement s'il est différent de l'email actuel
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email déjà utilisé");
        }

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User updatedUser = userRepository.save(user);

        return new UserProfileResponse(
                updatedUser.getId(),
                updatedUser.getEmail(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getRole()
        );
    }

    /**
     * Permet à l'utilisateur connecté de changer son mot de passe.
     * Vérifie le mot de passe actuel, puis hache et enregistre le nouveau.
     * Révoque tous les refresh tokens de l'utilisateur pour invalider les sessions actives.
     *
     * @param currentEmail email de l'utilisateur, extrait du contexte d'authentification
     * @param request      DTO contenant le mot de passe actuel et le nouveau mot de passe
     * @throws UserNotFoundException   si l'utilisateur est introuvable
     * @throws InvalidPasswordException si le mot de passe actuel fourni est incorrect
     */
    public void changeCurrentUserPassword(String currentEmail, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));

        boolean currentPasswordMatches = passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword()
        );

        if (!currentPasswordMatches) {
            throw new InvalidPasswordException("Mot de passe actuel incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Révocation de toutes les sessions pour forcer une reconnexion avec le nouveau mot de passe
        refreshTokenService.revokeAllUserRefreshTokens(user);
    }

    /**
     * Génère un nouvel access token JWT à partir d'un refresh token valide.
     * Le refresh token existant est réutilisé sans rotation pour cette implémentation.
     *
     * @param request DTO contenant le refresh token
     * @return {@link RefreshTokenResponse} avec le nouvel access token et le refresh token inchangé
     * @throws com.vinylove.backend.exception.InvalidRefreshTokenException si le refresh token est invalide, révoqué ou expiré
     */
    public RefreshTokenResponse refreshAccessToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());

        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return new RefreshTokenResponse(
                newAccessToken,
                refreshToken.getToken()
        );
    }

    /**
     * Déconnecte l'utilisateur en révoquant son refresh token.
     *
     * @param request DTO contenant le refresh token à révoquer
     * @throws com.vinylove.backend.exception.InvalidRefreshTokenException si le refresh token est introuvable
     */
    public void logout(LogoutRequest request) {
        refreshTokenService.revokeRefreshToken(request.getRefreshToken());
    }
}
