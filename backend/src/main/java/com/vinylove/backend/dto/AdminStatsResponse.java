package com.vinylove.backend.dto;

public class AdminStatsResponse {

    private long totalEvents;
    private long totalTables;
    private int totalCapacity;
    private int totalScans;
    private int remainingEntries;
    private double fillRate;

    public AdminStatsResponse(
            long totalEvents,
            long totalTables,
            int totalCapacity,
            int totalScans,
            int remainingEntries,
            double fillRate
    ) {
        this.totalEvents = totalEvents;
        this.totalTables = totalTables;
        this.totalCapacity = totalCapacity;
        this.totalScans = totalScans;
        this.remainingEntries = remainingEntries;
        this.fillRate = fillRate;
    }

    public long getTotalEvents() {
        return totalEvents;
    }

    public long getTotalTables() {
        return totalTables;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public int getTotalScans() {
        return totalScans;
    }

    public int getRemainingEntries() {
        return remainingEntries;
    }

    public double getFillRate() {
        return fillRate;
    }
}
