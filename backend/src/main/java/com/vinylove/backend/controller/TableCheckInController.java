package com.vinylove.backend.controller;

import com.vinylove.backend.dto.TableCheckInResponse;
import com.vinylove.backend.service.InvitationTableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/table-check-in")
public class TableCheckInController {

    private final InvitationTableService invitationTableService;

    public TableCheckInController(InvitationTableService invitationTableService) {
        this.invitationTableService = invitationTableService;
    }

    @GetMapping
    public ResponseEntity<TableCheckInResponse> checkInTable(@RequestParam String code) {
        TableCheckInResponse response = invitationTableService.checkInTableByQrCode(code);
        return ResponseEntity.ok(response);
    }
}
