package com.vinylove.backend.service;

import com.vinylove.backend.dto.InvitationTableResponse;
import com.vinylove.backend.entity.Event;
import com.vinylove.backend.entity.InvitationTable;
import com.vinylove.backend.exception.EventNotFoundException;
import com.vinylove.backend.exception.InvalidInvitationTableException;
import com.vinylove.backend.repository.InvitationTableRepository;
import com.vinylove.backend.dto.TableCheckInResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitationTableService {

    private final InvitationTableRepository invitationTableRepository;
    private final EventService eventService;

    public InvitationTableService(
            InvitationTableRepository invitationTableRepository,
            EventService eventService
    ) {
        this.invitationTableRepository = invitationTableRepository;
        this.eventService = eventService;
    }

    public InvitationTableResponse createInvitationTable(Long eventId, InvitationTable invitationTable) {
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Événement introuvable"));

        if (invitationTable.getLabel() == null || invitationTable.getLabel().isBlank()) {
            throw new InvalidInvitationTableException("Le nom de la table est obligatoire");
        }

        if (invitationTable.getGuestText() == null || invitationTable.getGuestText().isBlank()) {
            throw new InvalidInvitationTableException("La liste des invités est obligatoire");
        }

        if (invitationTable.getCapacity() <= 0) {
            throw new InvalidInvitationTableException("La capacité doit être supérieure à 0");
        }

        invitationTable.setEvent(event);
        invitationTable.setScanCount(0);

        InvitationTable savedTable = invitationTableRepository.save(invitationTable);

        return mapToResponse(savedTable);
    }

    public List<InvitationTableResponse> getInvitationTablesByEvent(Long eventId) {
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Événement introuvable"));

        return invitationTableRepository.findByEvent(event)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private InvitationTableResponse mapToResponse(InvitationTable invitationTable) {
        return new InvitationTableResponse(
                invitationTable.getId(),
                invitationTable.getLabel(),
                invitationTable.getGuestText(),
                invitationTable.getCapacity(),
                invitationTable.getScanCount(),
                invitationTable.getQrCode(),
                invitationTable.getEvent().getId(),
                invitationTable.getEvent().getName()
        );
    }

    public TableCheckInResponse checkInTableByQrCode(String qrCode) {
        InvitationTable invitationTable = invitationTableRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new InvalidInvitationTableException("Table d'invitation introuvable"));

        if (invitationTable.getScanCount() >= invitationTable.getCapacity()) {
            return new TableCheckInResponse(
                    "Capacité atteinte",
                    invitationTable.getId(),
                    invitationTable.getLabel(),
                    invitationTable.getCapacity(),
                    invitationTable.getScanCount(),
                    0,
                    false
            );
        }

        invitationTable.setScanCount(invitationTable.getScanCount() + 1);

        InvitationTable savedTable = invitationTableRepository.save(invitationTable);

        return new TableCheckInResponse(
                "Entrée validée",
                savedTable.getId(),
                savedTable.getLabel(),
                savedTable.getCapacity(),
                savedTable.getScanCount(),
                savedTable.getCapacity() - savedTable.getScanCount(),
                true
        );
    }

    public InvitationTable getInvitationTableById(Long tableId) {
        return invitationTableRepository.findById(tableId)
                .orElseThrow(() ->
                        new InvalidInvitationTableException("Table d'invitation introuvable"));
    }
}
