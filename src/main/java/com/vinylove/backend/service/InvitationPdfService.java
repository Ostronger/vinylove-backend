package com.vinylove.backend.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.vinylove.backend.entity.Guest;
import com.vinylove.backend.exception.GuestNotFoundException;
import com.vinylove.backend.repository.GuestRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class InvitationPdfService {

    private final GuestRepository guestRepository;
    private final QrCodeService qrCodeService;

    public InvitationPdfService(GuestRepository guestRepository, QrCodeService qrCodeService) {
        this.guestRepository = guestRepository;
        this.qrCodeService = qrCodeService;
    }

    public byte[] generateInvitationPdf(Long guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestNotFoundException("Invité introuvable"));

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);

            document.open();

            Font titleFont = new Font(Font.HELVETICA, 24, Font.BOLD);
            Font subtitleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 12);

            Paragraph title = new Paragraph("Invitation", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            Paragraph eventName = new Paragraph(guest.getEvent().getName(), subtitleFont);
            eventName.setAlignment(Element.ALIGN_CENTER);
            document.add(eventName);

            document.add(new Paragraph(" "));

            document.add(new Paragraph("Invité : " + guest.getFirstName() + " " + guest.getLastName(), normalFont));
            document.add(new Paragraph("Date : " + guest.getEvent().getEventDate(), normalFont));
            document.add(new Paragraph("Lieu : " + guest.getEvent().getLocation(), normalFont));

            document.add(new Paragraph(" "));

            byte[] qrCodeBytes = qrCodeService.generateQrCode(guest.getQrCode(), 250, 250);
            Image qrImage = Image.getInstance(qrCodeBytes);
            qrImage.setAlignment(Element.ALIGN_CENTER);
            document.add(qrImage);

            document.add(new Paragraph(" "));

            Paragraph instruction = new Paragraph("Présentez ce QR code à l'entrée.", normalFont);
            instruction.setAlignment(Element.ALIGN_CENTER);
            document.add(instruction);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF d'invitation");
        }
    }
}
