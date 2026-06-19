package com.vinylove.backend.controller;

import com.vinylove.backend.dto.AdminStatsResponse;
import com.vinylove.backend.dto.EventStatsResponse;
import com.vinylove.backend.service.AdminStatsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    public AdminStatsController(AdminStatsService adminStatsService) {
        this.adminStatsService = adminStatsService;
    }

    @GetMapping
    public AdminStatsResponse getGlobalStats() {
        return adminStatsService.getGlobalStats();
    }

    @GetMapping("/events")
    public List<EventStatsResponse> getEventStats() {
        return adminStatsService.getEventStats();
    }
}