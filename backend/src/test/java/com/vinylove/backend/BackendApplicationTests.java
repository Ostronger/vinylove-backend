package com.vinylove.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Classe de test vérifiant le démarrage correct du contexte Spring Boot.
 * Garantit que tous les beans sont correctement configurés et que l'application
 * peut démarrer sans erreur de configuration ou de dépendances manquantes.
 */
@SpringBootTest
class BackendApplicationTests {

    /**
     * Vérifie que le contexte Spring se charge sans exception au démarrage.
     * Ce test échoue si un bean est mal configuré ou si une dépendance est manquante.
     */
    @Test
    void contextLoads() {
    }
}
