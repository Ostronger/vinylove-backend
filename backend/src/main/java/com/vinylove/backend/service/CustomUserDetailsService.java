package com.vinylove.backend.service;

import com.vinylove.backend.entity.User;
import com.vinylove.backend.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation de {@link UserDetailsService} utilisée par Spring Security
 * pour charger les détails d'un utilisateur à partir de son email lors de l'authentification JWT.
 * Convertit l'entité {@link User} en un objet {@link UserDetails} reconnu par Spring Security,
 * en incluant le rôle préfixé par {@code ROLE_} (convention Spring Security).
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructeur avec injection du repository utilisateur.
     *
     * @param userRepository repository JPA pour l'accès aux données utilisateur
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charge un utilisateur par son email et le convertit en {@link UserDetails} pour Spring Security.
     * Le rôle est préfixé par {@code ROLE_} (ex : {@code ROLE_ADMIN}) conformément à la convention
     * attendue par {@code hasRole()} dans la configuration de sécurité.
     *
     * @param email adresse email de l'utilisateur (utilisée comme "username" dans Spring Security)
     * @return objet {@link UserDetails} contenant l'email, le mot de passe haché et les autorités
     * @throws UsernameNotFoundException si aucun utilisateur ne correspond à cet email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        // Préfixe ROLE_ requis par Spring Security pour que hasRole("ADMIN") fonctionne correctement
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
