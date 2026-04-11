package com.vinylove.backend;

import com.vinylove.backend.entity.User;
import com.vinylove.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
	/*
    @Bean
    CommandLineRunner testUserRepository(UserRepository userRepository) {
        return args -> {
            User user = new User();
            user.setEmail("test@vinylove.com");
            user.setPassword("123456");
            user.setFirstName("Pierre");
            user.setLastName("Jacque");
            user.setRole("ADMIN");

            userRepository.save(user);

            System.out.println("Utilisateur enregistré avec succès : " + user.getEmail());
        };
    }
		*/
} 
