package com.vinylove.backend.service;

import com.vinylove.backend.entity.User;
import com.vinylove.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vinylove.backend.exception.EmailAlreadyExistsException;
import com.vinylove.backend.dto.LoginRequest;
import com.vinylove.backend.dto.LoginResponse;
import com.vinylove.backend.dto.UpdateUserProfileRequest;
import com.vinylove.backend.exception.InvalidCredentialsException;
import com.vinylove.backend.dto.UserProfileResponse;
import com.vinylove.backend.exception.UserNotFoundException;
import com.vinylove.backend.dto.ChangePasswordRequest;
import com.vinylove.backend.exception.InvalidPasswordException;
import com.vinylove.backend.entity.RefreshToken;


import java.util.List;
import java.util.Optional;

@Service // Marks this class as a Spring service component
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public User saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email déjà utilisé");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        
        // Étape 1 : cherche l'utilisateur par email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Email ou mot de passe incorrect"));
        
        // Étape 2 : vérifie que le mot de passe correspond
        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new InvalidCredentialsException("Email ou mot de passe incorrect");
        }

        // Génère un token JWT pour l'utilisateur connecté
        String accessToken = jwtService.generateToken(user.getEmail(), user.getRole().name());
        // Génère un token de rafraîchissement
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        // Étape 3 : retourne les informations de l'utilisateur (sans le mot de passe)
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

    public UserProfileResponse UpdateCurrentUserProfile(String currentEmail, UpdateUserProfileRequest request) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));
            
        if (!user.getEmail().equals(request.getEmail()) 
                    && userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email déjà utilisé");
        } 
           
        // Met à jour les champs du profil de l'utilisateur
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        // Enregistre les modifications dans la base de données
        User updatedUser = userRepository.save(user);

        return new UserProfileResponse(
                updatedUser.getId(),
                updatedUser.getEmail(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getRole()
        );
    }

    public void changeCurrentUserPassword(String currentEmail, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(currentEmail) // Récupère l'utilisateur actuel à partir de son email
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));

        boolean currentPasswordMatches = passwordEncoder.matches( // Vérifie que le mot de passe actuel fourni correspond au mot de passe stocké dans la base de données
                request.getCurrentPassword(),
                user.getPassword()
        );

        if (!currentPasswordMatches) { // Si le mot de passe actuel ne correspond pas, lance une exception
            throw new InvalidPasswordException("Mot de passe actuel incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword())); // Si le mot de passe actuel est correct, encode le nouveau mot de passe et le met à jour dans l'entité User
        userRepository.save(user);
    }
}