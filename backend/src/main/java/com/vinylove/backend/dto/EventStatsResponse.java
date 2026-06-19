package com.vinylove.backend.dto;

public class EventStatsResponse {

    private Long eventId;
    private String eventName;
    private int capacity;
    private int scans;
    private int remaining;
    private double fillRate;

    public EventStatsResponse(
            Long eventId,
            String eventName,
            int capacity,
            int scans,
            int remaining,
            double fillRate
    ) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.capacity = capacity;
        this.scans = scans;
        this.remaining = remaining;
        this.fillRate = fillRate;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getScans() {
        return scans;
    }

    public int getRemaining() {
        return remaining;
    }

    public double getFillRate() {
        return fillRate;
    }
}
