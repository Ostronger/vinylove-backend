package com.vinylove.backend.controller;

import com.vinylove.backend.dto.InvitationTableResponse;
import com.vinylove.backend.entity.InvitationTable;
import com.vinylove.backend.service.InvitationTablePdfService;
import com.vinylove.backend.service.InvitationTableService;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/events/{eventId}/invitation-tables")
public class InvitationTableController {

    private final InvitationTableService invitationTableService;
    private final InvitationTablePdfService invitationTablePdfService;

    public InvitationTableController(InvitationTableService invitationTableService, InvitationTablePdfService invitationTablePdfService) {
        this.invitationTableService = invitationTableService;
        this.invitationTablePdfService = invitationTablePdfService;
    }

    @PostMapping
    public InvitationTableResponse createInvitationTable(
            @PathVariable Long eventId,
            @RequestBody InvitationTable invitationTable
    ) {
        return invitationTableService.createInvitationTable(eventId, invitationTable);
    }

    @GetMapping
    public List<InvitationTableResponse> getInvitationTablesByEvent(@PathVariable Long eventId) {
        return invitationTableService.getInvitationTablesByEvent(eventId);
    }

    @GetMapping(value = "/{tableId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getInvitationTablePdf(@PathVariable Long tableId) {
        byte[] pdf = invitationTablePdfService.generateInvitationTablePdf(tableId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=invitation-table-" + tableId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
