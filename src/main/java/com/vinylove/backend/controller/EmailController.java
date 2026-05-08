package com.vinylove.backend.controller;

import com.vinylove.backend.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emails")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    // Endpoint de test pour envoyer un email
    @PostMapping("/test")
    public ResponseEntity<String> sendTestEmail(@RequestParam String to) {
        emailService.sendTestEmail(to);
        return ResponseEntity.ok("Test email sent envoyé avec succès");
    }
}
