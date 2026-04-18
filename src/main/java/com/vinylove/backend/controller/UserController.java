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

@RestController // Marks this class as a REST controller
@RequestMapping("/api/users") // Base URL for all user-related endpoints
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping // Endpoint to get all users
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}") // Endpoint to get a user by ID
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping // Endpoint to create a new user
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @DeleteMapping("/{id}") // Endpoint to delete a user by ID
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/login") // Endpoint for user login
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me") // Endpoint to get the profile of the currently authenticated user
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile(Authentication authentication) {
        String email = authentication.getName();
        UserProfileResponse response = userService.getCurrentUserProfile(email);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/password") // Endpoint to change the password of the currently authenticated user
    public ResponseEntity<String> changeCurrentUserPassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest request
    ) {
        String email = authentication.getName();
        userService.changeCurrentUserPassword(email, request);
        return ResponseEntity.ok("Mot de passe changé avec succès");        
    }
    
    @PostMapping("/refresh-token") // Endpoint to refresh JWT tokens
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = userService.refreshAccessToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {
        userService.logout(request);
        return ResponseEntity.ok("Déconnexion réussie");
}
}
