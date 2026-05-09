package com.vinylove.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinylove.backend.dto.CheckInResponse;
import com.vinylove.backend.service.GuestService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/check-in")
public class CheckInController {
    
    private final GuestService guestService;

    public CheckInController(GuestService guestService) {
        this.guestService = guestService;
    }

    
    @GetMapping
    public ResponseEntity<CheckInResponse> checkIn(@RequestParam String code) {
        CheckInResponse response = guestService.checkInByQrCode(code);
        return ResponseEntity.ok(response);
    }
    
}
