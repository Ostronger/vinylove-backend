package com.vinylove.backend.service;

import com.vinylove.backend.entity.InvitationTable;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class InvitationTablePdfService {

    private final InvitationTableService invitationTableService;
    private final QrCodeService qrCodeService;

    public InvitationTablePdfService(
            InvitationTableService invitationTableService,
            QrCodeService qrCodeService
    ) {
        this.invitationTableService = invitationTableService;
        this.qrCodeService = qrCodeService;
    }

    public byte[] generateInvitationTablePdf(Long tableId) {

        InvitationTable table = invitationTableService.getInvitationTableById(tableId);

        try {
            byte[] qrCodeBytes = qrCodeService.generateQrCode(table.getQrCode(), 250, 250);
            String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCodeBytes);

            String bannerHtml = "";
            String bannerImageUrl = table.getEvent().getBannerImageUrl();

            if (bannerImageUrl != null && !bannerImageUrl.isBlank()) {
                bannerHtml = "<div class=\"banner\"><img src=\"" + bannerImageUrl + "\" /></div>";
            }

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd MMMM yyyy 'à' HH:mm", Locale.FRENCH);

            String eventDate = table.getEvent()
                    .getEventDate()
                    .format(formatter);
            String eventName = table.getEvent().getName();
            String eventLocation = table.getEvent().getLocation();

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

                    "    .banner { width: 100%%; height: 220px; overflow: hidden; background-color: #120D14; }" +
                    "    .banner img { width: 100%%; height: 220px; }" +

                    "    .body { padding: 25px 45px 75px 45px; background-color: #FFFFFF; }" +

                    "    .title-invitation {" +
                    "      text-align: center;" +
                    "      font-family: Georgia, serif;" +
                    "      font-size: 32px;" +
                    "      font-weight: bold;" +
                    "      color: #C9A84C;" +
                    "      letter-spacing: 6px;" +
                    "      text-transform: uppercase;" +
                    "      margin-bottom: 6px;" +
                    "    }" +

                    "    .subtitle {" +
                    "      text-align: center;" +
                    "      font-size: 11px;" +
                    "      letter-spacing: 3px;" +
                    "      color: #888888;" +
                    "      text-transform: uppercase;" +
                    "      margin-bottom: 10px;" +
                    "    }" +

                    "    .separator {" +
                    "      text-align: center;" +
                    "      color: #C9A84C;" +
                    "      font-size: 14px;" +
                    "      margin-bottom: 10px;" +
                    "    }" +

                    "    .event-name {" +
                    "      text-align: center;" +
                    "      font-size: 21px;" +
                    "      font-weight: bold;" +
                    "      color: #120D14;" +
                    "      letter-spacing: 2px;" +
                    "      text-transform: uppercase;" +
                    "      margin-bottom: 18px;" +
                    "    }" +

                    "    .info-table { width: 100%%; border-collapse: collapse; margin-bottom: 14px; border: 1px solid #e8e8e8; }" +
                    "    .info-cell { width: 50%%; padding: 14px 18px; vertical-align: top; border: 1px solid #e8e8e8; }" +
                    "    .info-label {" +
                    "      font-size: 9px;" +
                    "      letter-spacing: 2px;" +
                    "      color: #C9A84C;" +
                    "      text-transform: uppercase;" +
                    "      font-weight: bold;" +
                    "      margin-bottom: 6px;" +
                    "    }" +
                    "    .info-value { font-size: 14px; color: #120D14; font-weight: bold; }" +

                    "    .table-block {" +
                    "      background-color: #F9F7F4;" +
                    "      border: 1px solid #e8e8e8;" +
                    "      text-align: center;" +
                    "      padding: 14px;" +
                    "      margin-bottom: 14px;" +
                    "    }" +
                    "    .table-label {" +
                    "      font-size: 9px;" +
                    "      letter-spacing: 3px;" +
                    "      color: #C9A84C;" +
                    "      text-transform: uppercase;" +
                    "      margin-bottom: 8px;" +
                    "    }" +
                    "    .table-name { font-size: 27px; color: #120D14; font-style: italic; }" +

                    "    .guest-text-block {" +
                    "      border: 1px solid #e8e8e8;" +
                    "      padding: 12px 14px;" +
                    "      margin-bottom: 14px;" +
                    "      background-color: #FFFFFF;" +
                    "    }" +
                    "    .guest-text-title {" +
                    "      font-size: 9px;" +
                    "      letter-spacing: 2px;" +
                    "      color: #C9A84C;" +
                    "      text-transform: uppercase;" +
                    "      font-weight: bold;" +
                    "      margin-bottom: 8px;" +
                    "    }" +
                    "    .guest-text {" +
                    "      font-size: 13px;" +
                    "      color: #120D14;" +
                    "      line-height: 1.5;" +
                    "      white-space: pre-line;" +
                    "    }" +

                    "    .qr-block { text-align: center; margin-bottom: 18px; }" +
                    "    .qr-border {" +
                    "      display: inline-block;" +
                    "      border: 2px solid #C9A84C;" +
                    "      padding: 12px;" +
                    "      background-color: #FFFFFF;" +
                    "    }" +
                    "    .qr-border img { width: 140px; height: 140px; }" +
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

                    "    .footer {" +
                    "      position: fixed;" +
                    "      bottom: 0;" +
                    "      left: 0;" +
                    "      right: 0;" +
                    "      width: 100%%;" +
                    "      background-color: #120D14;" +
                    "      padding: 12px 40px;" +
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

                    "%s" +

                    "<div class=\"body\">" +

                    "  <p class=\"title-invitation\">Invitation</p>" +
                    "  <p class=\"subtitle\">Vous &#234;tes convi&#233;(e) &#224;</p>" +
                    "  <p class=\"separator\">&#8212; &#10022; &#8212;</p>" +
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

                    "  <div class=\"table-block\">" +
                    "    <p class=\"table-label\">Invitation</p>" +
                    "    <p class=\"table-name\">%s</p>" +
                    "  </div>" +

                    "  <div class=\"info-table\">" +
                    "    <table style=\"width: 100%%; border-collapse: collapse;\">" +
                    "      <tr>" +
                    "        <td class=\"info-cell\">" +
                    "          <p class=\"info-label\">Capacit&#233;</p>" +
                    "          <p class=\"info-value\">%d entr&#233;e(s)</p>" +
                    "        </td>" +
                    "        <td class=\"info-cell\">" +
                    "          <p class=\"info-label\">QR Code</p>" +
                    "          <p class=\"info-value\">Valable jusqu'&#224; capacit&#233;</p>" +
                    "        </td>" +
                    "      </tr>" +
                    "    </table>" +
                    "  </div>" +

                    "  <div class=\"guest-text-block\">" +
                    "    <p class=\"guest-text-title\">Invit&#233;s / Notes</p>" +
                    "    <p class=\"guest-text\">%s</p>" +
                    "  </div>" +

                    "  <div class=\"qr-block\">" +
                    "    <div class=\"qr-border\">" +
                    "      <img src=\"data:image/png;base64,%s\" />" +
                    "    </div>" +
                    "    <p class=\"qr-label\">Pr&#233;sentez ce QR code &#224; l'entr&#233;e</p>" +
                    "    <p class=\"qr-sublabel\">Ce code autorise plusieurs entr&#233;es selon la capacit&#233; indiqu&#233;e.</p>" +
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
                    table.getLabel(),
                    table.getCapacity(),
                    escapeHtml(table.getGuestText()).replace("\n", "<br/>"),
                    qrCodeBase64
            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF de table d'invitation", e);
        }
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
