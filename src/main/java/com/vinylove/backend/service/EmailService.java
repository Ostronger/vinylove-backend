package com.vinylove.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTestEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Test Email from Vinylove");
        message.setText("This is a test email sent from the Vinylove backend service.");
        
        mailSender.send(message);   
        
    }

    public void sendGuestInvitation(
            String to,
            String guestFullName,
            String eventName,
            String eventDate,
            String eventLocation,
            String qrCodeUrl
    ) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Votre invitation - " + eventName);

        message.setText(
                "Bonjour " + guestFullName + ",\n\n" +
                "Vous êtes invité à l'événement : " + eventName + "\n" +
                "Date : " + eventDate + "\n" +
                "Lieu : " + eventLocation + "\n\n" +
                "Votre QR code est disponible ici :\n" +
                qrCodeUrl + "\n\n" +
                "Présentez ce QR code à l'entrée.\n\n" +
                "Cordialement,\n" +
                "L'équipe VinyLove"
        );

        mailSender.send(message);
    }
}
