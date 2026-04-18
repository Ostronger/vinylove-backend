package com.vinylove.backend.service;

import com.vinylove.backend.entity.User;
import com.vinylove.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vinylove.backend.exception.EmailAlreadyExistsException;
import com.vinylove.backend.dto.LoginRequest;
import com.vinylove.backend.dto.LoginResponse;
import com.vinylove.backend.exception.InvalidCredentialsException;

import java.util.List;
import java.util.Optional;

@Service // Marks this class as a Spring service component
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        // Étape 3 : retourne les informations de l'utilisateur (sans le mot de passe)
        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                "Connexion réussie",
                token
        );
    }
}