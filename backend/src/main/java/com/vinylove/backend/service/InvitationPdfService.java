/**  version 1
package com.vinylove.backend.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.vinylove.backend.entity.Guest;
import com.vinylove.backend.exception.GuestNotFoundException;
import com.vinylove.backend.repository.GuestRepository;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

@Service
public class InvitationPdfService {

    private final GuestRepository guestRepository;
    private final QrCodeService qrCodeService;

    public InvitationPdfService(
            GuestRepository guestRepository,
            QrCodeService qrCodeService
    ) {
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

            // =========================
            // Bannière événement
            // =========================

            String bannerImageUrl = guest.getEvent().getBannerImageUrl();

            if (bannerImageUrl != null && !bannerImageUrl.isBlank()) {

                Image banner = Image.getInstance(bannerImageUrl);

                banner.scaleToFit(520, 180);
                banner.setAlignment(Element.ALIGN_CENTER);

                document.add(banner);
                document.add(new Paragraph(" "));
            }

            // =========================
            // Header VIP
            // =========================

            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setWidthPercentage(100);

            PdfPCell titleCell = new PdfPCell(
                    new Phrase(
                            "VIP INVITATION",
                            new Font(Font.HELVETICA, 26, Font.BOLD, Color.WHITE)
                    )
            );

            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            titleCell.setBackgroundColor(new Color(139, 30, 63));
            titleCell.setPadding(20);
            titleCell.setBorder(Rectangle.NO_BORDER);

            titleTable.addCell(titleCell);

            document.add(titleTable);

            document.add(new Paragraph(" "));

            // =========================
            // Carte infos événement
            // =========================

            PdfPTable infoCard = new PdfPTable(1);

            infoCard.setWidthPercentage(90);
            infoCard.setSpacingBefore(20);
            infoCard.setSpacingAfter(20);

            String infoText =
                    "Événement : " + guest.getEvent().getName() + "\n\n" +
                    "Invité : " + guest.getFirstName() + " " + guest.getLastName() + "\n\n" +
                    "Date : " + guest.getEvent().getEventDate() + "\n\n" +
                    "Lieu : " + guest.getEvent().getLocation();

            PdfPCell infoCell = new PdfPCell(
                    new Phrase(
                            infoText,
                            new Font(Font.HELVETICA, 13, Font.NORMAL, new Color(40, 40, 40))
                    )
            );

            infoCell.setPadding(18);
            infoCell.setBorderColor(new Color(230, 230, 230));
            infoCell.setBackgroundColor(new Color(250, 250, 250));
            infoCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            infoCard.addCell(infoCell);

            document.add(infoCard);

            // =========================
            // QR Code premium
            // =========================

            PdfPTable qrCard = new PdfPTable(1);

            qrCard.setWidthPercentage(45);
            qrCard.setSpacingBefore(10);
            qrCard.setSpacingAfter(20);

            PdfPCell qrTitleCell = new PdfPCell(
                    new Phrase(
                            "Votre QR Code",
                            new Font(Font.HELVETICA, 14, Font.BOLD, new Color(139, 30, 63))
                    )
            );

            qrTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            qrTitleCell.setBorder(Rectangle.NO_BORDER);
            qrTitleCell.setPaddingBottom(10);

            qrCard.addCell(qrTitleCell);

            byte[] qrCodeBytes = qrCodeService.generateQrCode(
                    guest.getQrCode(),
                    250,
                    250
            );

            Image qrImage = Image.getInstance(qrCodeBytes);

            qrImage.scaleToFit(190, 190);

            PdfPCell qrImageCell = new PdfPCell(qrImage);

            qrImageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            qrImageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            qrImageCell.setPadding(16);
            qrImageCell.setBorderColor(new Color(220, 220, 220));
            qrImageCell.setBackgroundColor(Color.WHITE);

            qrCard.addCell(qrImageCell);

            document.add(qrCard);

            // =========================
            // Instruction
            // =========================

            Font normalFont = new Font(Font.HELVETICA, 12);

            Paragraph instruction = new Paragraph(
                    "Présentez ce QR code à l'entrée.",
                    normalFont
            );

            instruction.setAlignment(Element.ALIGN_CENTER);

            document.add(instruction);

            // =========================
            // Footer
            // =========================

            document.add(new Paragraph(" "));

            PdfPTable footerTable = new PdfPTable(1);

            footerTable.setWidthPercentage(100);
            footerTable.setSpacingBefore(30);

            PdfPCell footerCell = new PdfPCell(
                    new Phrase(
                            "VinyLove Event • Invitation officielle",
                            new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(120, 120, 120))
                    )
            );

            footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            footerCell.setBorder(Rectangle.TOP);
            footerCell.setBorderColor(new Color(230, 230, 230));
            footerCell.setPaddingTop(12);

            footerTable.addCell(footerCell);

            document.add(footerTable);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF d'invitation");
        }
    }
}
    */

package com.vinylove.backend.service;

import com.vinylove.backend.entity.Guest;
import com.vinylove.backend.exception.GuestNotFoundException;
import com.vinylove.backend.repository.GuestRepository;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;

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
            // QR Code → base64
            byte[] qrCodeBytes = qrCodeService.generateQrCode(guest.getQrCode(), 250, 250);
            String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCodeBytes);

            // Bannière
            String bannerHtml = "";
            String bannerImageUrl = guest.getEvent().getBannerImageUrl();
            if (bannerImageUrl != null && !bannerImageUrl.isBlank()) {
                bannerHtml = "<div class=\"banner\"><img src=\"" + bannerImageUrl + "\" /></div>";
            }

            // Données événement
            String guestName    = guest.getFirstName() + " " + guest.getLastName();
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd MMMM yyyy 'à' HH:mm", Locale.FRENCH);

            String eventDate = guest.getEvent()
                    .getEventDate()
                    .format(formatter);
            String eventName    = guest.getEvent().getName();
            String eventLocation = guest.getEvent().getLocation();

            // ─── HTML COMPLET ──────────────────────────────────────────────────────────
            String html = String.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" " +
                "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "  <meta charset=\"UTF-8\"/>" +
                "  <style>" +
                "    @page { size: A4; margin: 0; }" +
                "    * { margin: 0; padding: 0; box-sizing: border-box; }" +
                "    body { font-family: Georgia, serif; background-color: #FFFFFF; color: #1a1a1a; width: 210mm; }" +

                // ── Bannière ──
                "    .banner { width: 100%%; height: 280px; overflow: hidden; background-color: #120D14; }" +
                "    .banner img { width: 100%%; height: 280px; }" +

                // ── Corps ──
                "    .body { padding: 40px 50px 90px 50px; background-color: #FFFFFF; }" +

                // ── Titre ──
                "    .title-invitation {" +
                "      text-align: center;" +
                "      font-family: Georgia, serif;" +
                "      font-size: 38px;" +
                "      font-weight: bold;" +
                "      color: #C9A84C;" +
                "      letter-spacing: 6px;" +
                "      text-transform: uppercase;" +
                "      margin-bottom: 8px;" +
                "    }" +

                // ── Sous-titre ──
                "    .subtitle {" +
                "      text-align: center;" +
                "      font-size: 11px;" +
                "      letter-spacing: 3px;" +
                "      color: #888888;" +
                "      text-transform: uppercase;" +
                "      margin-bottom: 10px;" +
                "    }" +

                // ── Séparateur ──
                "    .separator {" +
                "      text-align: center;" +
                "      color: #C9A84C;" +
                "      font-size: 14px;" +
                "      margin-bottom: 10px;" +
                "    }" +

                // ── Nom événement ──
                "    .event-name {" +
                "      text-align: center;" +
                "      font-size: 24px;" +
                "      font-weight: bold;" +
                "      color: #120D14;" +
                "      letter-spacing: 2px;" +
                "      text-transform: uppercase;" +
                "      margin-bottom: 30px;" +
                "    }" +

                // ── Tableau infos ──
                "    .info-table { width: 100%%; border-collapse: collapse; margin-bottom: 28px; border: 1px solid #e8e8e8; }" +
                "    .info-cell { width: 50%%; padding: 16px 20px; vertical-align: top; border: 1px solid #e8e8e8; }" +
                "    .info-label {" +
                "      font-size: 9px;" +
                "      letter-spacing: 2px;" +
                "      color: #C9A84C;" +
                "      text-transform: uppercase;" +
                "      font-weight: bold;" +
                "      margin-bottom: 6px;" +
                "    }" +
                "    .info-value { font-size: 14px; color: #120D14; font-weight: bold; }" +

                // ── Bloc invité ──
                "    .guest-block {" +
                "      background-color: #F9F7F4;" +
                "      border: 1px solid #e8e8e8;" +
                "      text-align: center;" +
                "      padding: 20px;" +
                "      margin-bottom: 28px;" +
                "    }" +
                "    .guest-label {" +
                "      font-size: 9px;" +
                "      letter-spacing: 3px;" +
                "      color: #C9A84C;" +
                "      text-transform: uppercase;" +
                "      margin-bottom: 8px;" +
                "    }" +
                "    .guest-name { font-size: 28px; color: #120D14; font-style: italic; }" +

                // ── QR Code ──
                "    .qr-block { text-align: center; margin-bottom: 20px; }" +
                "    .qr-border {" +
                "      display: inline-block;" +
                "      border: 2px solid #C9A84C;" +
                "      padding: 12px;" +
                "      background-color: #FFFFFF;" +
                "    }" +
                "    .qr-border img { width: 160px; height: 160px; }" +
                "    .qr-label {" +
                "      text-align: center;" +
                "      font-size: 10px;" +
                "      letter-spacing: 2px;" +
                "      color: #555555;" +
                "      text-transform: uppercase;" +
                "      margin-top: 10px;" +
                "    }" +
                "    .qr-sublabel {" +
                "      text-align: center;" +
                "      font-size: 10px;" +
                "      color: #aaaaaa;" +
                "      font-style: italic;" +
                "      margin-top: 4px;" +
                "    }" +

                // ── Footer ──
                "    .footer {" +
                "      position: fixed;" +
                "      bottom: 0;" +
                "      left: 0;" +
                "      right: 0;" +
                "      width: 100%%;" +
                "      background-color: #120D14;" +
                "      padding: 16px 40px;" +
                "      text-align: center;" +
                "    }" +
                "    .footer-line {" +
                "      width: 40px;" +
                "      height: 1px;" +
                "      background-color: #C9A84C;" +
                "      display: inline-block;" +
                "      margin: 0 12px;" +
                "      vertical-align: middle;" +
                "    }" +
                "    .footer-text {" +
                "      font-size: 11px;" +
                "      letter-spacing: 3px;" +
                "      color: #C9A84C;" +
                "      text-transform: uppercase;" +
                "    }" +
                "  </style>" +
                "</head>" +
                "<body>" +

                // Bannière (optionnelle)
                "%s" +

                "<div class=\"body\">" +

                "  <p class=\"title-invitation\">Invitation</p>" +
                "  <p class=\"subtitle\">Vous &#234;tes convi&#233;(e) &#224;</p>" +
                "  <p class=\"separator\">&#8212; &#10022; •  &#8212;</p>" +
                "  <p class=\"event-name\">%s</p>" +

                "  <table class=\"info-table\">" +
                "    <tr>" +
                "      <td class=\"info-cell\">" +
                "        <p class=\"info-label\">Date</p>" +
                "        <p class=\"info-value\">%s</p>" +
                "      </td>" +
                "      <td class=\"info-cell\">" +
                "        <p class=\"info-label\">Lieu</p>" +
                "        <p class=\"info-value\">%s</p>" +
                "      </td>" +
                "    </tr>" +
                "  </table>" +

                "  <div class=\"guest-block\">" +
                "    <p class=\"guest-label\">Invit&#233;(e)</p>" +
                "    <p class=\"guest-name\">%s</p>" +
                "  </div>" +

                "  <div class=\"qr-block\">" +
                "    <div class=\"qr-border\">" +
                "      <img src=\"data:image/png;base64,%s\" />" +
                "    </div>" +
                "    <p class=\"qr-label\">Pr&#233;sentez ce QR code &#224; l'entr&#233;e</p>" +
                "    <p class=\"qr-sublabel\">Ce code est unique et personnel.</p>" +
                "  </div>" +

                "</div>" +

                "<div class=\"footer\">" +
                "  <span class=\"footer-line\">&#160;</span>" +
                "  <span class=\"footer-text\">VinyLove Event • Invitation officielle</span>" +
                "  <span class=\"footer-line\">&#160;</span>" +
                "</div>" +

                "</body>" +
                "</html>",

                bannerHtml,
                eventName,
                eventDate,
                eventLocation,
                guestName,
                qrCodeBase64
            );

            // ─── GÉNÉRATION PDF ────────────────────────────────────────────────────────
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF d'invitation", e);
        }
    }
}


