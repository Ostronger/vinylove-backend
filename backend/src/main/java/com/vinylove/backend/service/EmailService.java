package com.vinylove.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ByteArrayResource;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

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
            byte[] qrCodeImage,
            byte[] invitationPdf
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Votre invitation - " + eventName);

            String htmlContent = """
                    <div style="background: #120D14; max-width: 600px; margin: auto; font-family: Georgia, serif; border: 1px solid #3a2030; border-radius: 4px; overflow: hidden;">

                        <!-- HEADER -->
                        <div style="background: linear-gradient(135deg, #1a0a12 0%%, #6B1428 50%%, #1a0a12 100%%); padding: 48px 40px 40px; text-align: center; border-bottom: 1px solid #D9BA80;">
                            <div style="height: 3px; background: linear-gradient(90deg, transparent 0%%, #D9BA80 50%%, transparent 100%%); margin-bottom: 28px;"></div>
                            <p style="font-family: 'Courier New', monospace; font-size: 10px; letter-spacing: 5px; color: #D9BA80; margin: 0 0 20px; text-transform: uppercase;">Agence Événementielle · Île-de-France</p>
                            <h1 style="font-family: Georgia, serif; font-size: 36px; font-weight: normal; color: #F7F2E6; margin: 0; letter-spacing: 3px; text-transform: uppercase;">Viny Love Even't</h1>
                            <div style="width: 60px; height: 1px; background: #D9BA80; margin: 24px auto 0;"></div>
                        </div>

                        <!-- BODY -->
                        <div style="padding: 48px 40px; background: #120D14;">

                            <p style="font-family: 'Courier New', monospace; font-size: 10px; letter-spacing: 4px; color: #D9BA80; text-transform: uppercase; margin: 0 0 32px;">Votre invitation personnelle</p>

                            <p style="font-family: Georgia, serif; font-size: 18px; color: #F7F2E6; margin: 0 0 12px; font-style: italic;">Bonjour <strong style="font-style: normal; color: #E8C9C7;">%s</strong>,</p>

                            <p style="font-family: Georgia, serif; font-size: 15px; color: #99918C; line-height: 1.8; margin: 0 0 36px;">Nous avons le plaisir de vous convier à un moment qui promet d'être inoubliable.</p>

                        <!-- Event card -->
                        <div style="border: 1px solid #3a2030; border-left: 3px solid #D9BA80; background: rgba(107,20,40,0.12); padding: 28px 32px; margin: 0 0 36px; border-radius: 2px;">
                            <p style="font-family: 'Courier New', monospace; font-size: 9px; letter-spacing: 3px; color: #D9BA80; text-transform: uppercase; margin: 0 0 12px;">Événement</p>
                            <h2 style="font-family: Georgia, serif; font-size: 26px; font-weight: normal; color: #F7F2E6; margin: 0 0 24px; letter-spacing: 1px;">%s</h2>
                            <p style="font-family: 'Courier New', monospace; font-size: 9px; letter-spacing: 2px; color: #D9BA80; text-transform: uppercase; margin: 0 0 6px;">Date</p>
                            <p style="font-family: Georgia, serif; font-size: 15px; color: #E8C9C7; margin: 0 0 16px;">%s</p>
                            <p style="font-family: 'Courier New', monospace; font-size: 9px; letter-spacing: 2px; color: #D9BA80; text-transform: uppercase; margin: 0 0 6px;">Lieu</p>
                            <p style="font-family: Georgia, serif; font-size: 15px; color: #E8C9C7; margin: 0;">%s</p>
                        </div>

                        <!-- QR Code -->
                        <div style="text-align: center; margin: 40px 0;">
                            <p style="font-family: 'Courier New', monospace; font-size: 9px; letter-spacing: 3px; color: #D9BA80; text-transform: uppercase; margin: 0 0 20px;">Votre laissez-passer</p>
                            <div style="display: inline-block; border: 1px solid #3a2030; padding: 16px; background: #fff; border-radius: 2px;">
                                <img src="cid:qrcode" alt="QR Code d'invitation" style="width: 180px; height: 180px; display: block;" />
                            </div>
                            <p style="font-family: Georgia, serif; font-size: 13px; color: #99918C; font-style: italic; margin: 16px 0 0;">Présentez ce QR code à l'entrée de l'événement.</p>
                        </div>

                        <div style="border-top: 1px solid #2a1520; margin: 40px 0;"></div>

                        <p style="font-family: Georgia, serif; font-size: 15px; color: #99918C; line-height: 1.8; margin: 0 0 8px;">Nous nous réjouissons de vous accueillir,</p>
                        <p style="font-family: Georgia, serif; font-size: 18px; color: #F7F2E6; font-style: italic; margin: 0;">L'équipe Viny Love Even't</p>
                        </div>

                        <!-- FOOTER -->
                            <div style="background: #0a060c; padding: 24px 40px; text-align: center; border-top: 1px solid #2a1520;">
                            <div style="width: 40px; height: 1px; background: #D9BA80; margin: 0 auto 16px;"></div>
                            <p style="font-family: 'Courier New', monospace; font-size: 9px; letter-spacing: 3px; color: #3a2030; text-transform: uppercase; margin: 0;">© 2025 Viny Love Even't · Tous droits réservés</p>
                        </div>

                    </div>
                    """.formatted(
                    guestFullName,
                    eventName,
                    eventDate,
                    eventLocation,
                    qrCodeImage
            );

            helper.setText(htmlContent, true);

            // Ajouter le QR code en pièce jointe avec un Content-ID pour l'afficher dans le corps de l'email
            helper.addInline("qrcode", new ByteArrayResource(qrCodeImage), "image/png");
            // Ajouter le PDF d'invitation en pièce jointe
            helper.addAttachment("invitation-" + guestFullName.replace(" ", "-").toLowerCase() + ".pdf", new ByteArrayResource(invitationPdf), "application/pdf");

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'invitation par email");
        }
    }
}
