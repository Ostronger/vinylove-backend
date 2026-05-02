package com.vinylove.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Point d'entrée principal de l'application Spring Boot Vinylove.
 * Lance le contexte Spring et initialise tous les composants de l'application.
 */
@SpringBootApplication
@EnableConfigurationProperties
public class BackendApplication {

    /**
     * Méthode principale qui démarre l'application Spring Boot.
     *
     * @param args arguments de la ligne de commande passés au démarrage
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
