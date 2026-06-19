package com.vinylove.backend.service;

import com.vinylove.backend.dto.AdminStatsResponse;
import com.vinylove.backend.dto.EventStatsResponse;
import com.vinylove.backend.dto.InvitationTableStatsResponse;
import com.vinylove.backend.entity.Event;
import com.vinylove.backend.entity.InvitationTable;
import com.vinylove.backend.exception.EventNotFoundException;
import com.vinylove.backend.repository.EventRepository;
import com.vinylove.backend.repository.InvitationTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminStatsService {

    private final EventRepository eventRepository;
    private final InvitationTableRepository invitationTableRepository;

    public AdminStatsService(
            EventRepository eventRepository,
            InvitationTableRepository invitationTableRepository
    ) {
        this.eventRepository = eventRepository;
        this.invitationTableRepository = invitationTableRepository;
    }

    public AdminStatsResponse getGlobalStats() {

        long totalEvents = eventRepository.count();

        List<InvitationTable> tables = invitationTableRepository.findAll();

        long totalTables = tables.size();

        int totalCapacity = tables.stream()
                .mapToInt(InvitationTable::getCapacity)
                .sum();

        int totalScans = tables.stream()
                .mapToInt(InvitationTable::getScanCount)
                .sum();

        int remainingEntries = totalCapacity - totalScans;

        double fillRate = totalCapacity == 0
                ? 0
                : ((double) totalScans / totalCapacity) * 100;

        return new AdminStatsResponse(
                totalEvents,
                totalTables,
                totalCapacity,
                totalScans,
                remainingEntries,
                Math.round(fillRate * 100.0) / 100.0
        );
    }

    public List<EventStatsResponse> getEventStats() {
        List<Event> events = eventRepository.findAll();

        return events.stream()
                .map(event -> {
                    List<InvitationTable> tables = invitationTableRepository.findByEvent(event);

                    int capacity = tables.stream()
                            .mapToInt(InvitationTable::getCapacity)
                            .sum();

                    int scans = tables.stream()
                            .mapToInt(InvitationTable::getScanCount)
                            .sum();

                    int remaining = capacity - scans;

                    double fillRate = capacity == 0
                            ? 0
                            : ((double) scans / capacity) * 100;

                    return new EventStatsResponse(
                            event.getId(),
                            event.getName(),
                            capacity,
                            scans,
                            remaining,
                            Math.round(fillRate * 100.0) / 100.0
                    );
                })
                .toList();
    }

    public EventStatsResponse getStatsForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Événement introuvable"));

        List<InvitationTable> tables = invitationTableRepository.findByEvent(event);

        int capacity = tables.stream()
                .mapToInt(InvitationTable::getCapacity)
                .sum();

        int scans = tables.stream()
                .mapToInt(InvitationTable::getScanCount)
                .sum();

        int remaining = capacity - scans;

        double fillRate = capacity == 0
                ? 0
                : ((double) scans / capacity) * 100;

        return new EventStatsResponse(
                event.getId(),
                event.getName(),
                capacity,
                scans,
                remaining,
                Math.round(fillRate * 100.0) / 100.0
        );
    }

    public List<InvitationTableStatsResponse> getTableStatsForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Événement introuvable"));

        List<InvitationTable> tables = invitationTableRepository.findByEvent(event);

        return tables.stream()
                .map(table -> {
                    int capacity = table.getCapacity();
                    int scans = table.getScanCount();
                    int remaining = capacity - scans;
                    double fillRate = capacity == 0
                            ? 0
                            : ((double) scans / capacity) * 100;

                    return new InvitationTableStatsResponse(
                            table.getId(),
                            table.getLabel(),
                            capacity,
                            scans,
                            remaining,
                            Math.round(fillRate * 100.0) / 100.0
                    );
                })
                .toList();
    }


}
