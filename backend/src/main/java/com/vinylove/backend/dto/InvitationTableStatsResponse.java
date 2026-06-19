package com.vinylove.backend.dto;

public class InvitationTableStatsResponse {

    private Long tableId;
    private String label;
    private int capacity;
    private int scans;
    private int remaining;
    private double fillRate;

    public InvitationTableStatsResponse(Long tableId, String label, int capacity, int scans, int remaining, double fillRate) {
        this.tableId = tableId;
        this.label = label;
        this.capacity = capacity;
        this.scans = scans;
        this.remaining = remaining;
        this.fillRate = fillRate;
    }

    public Long getTableId() { return tableId; }
    public String getLabel() { return label; }
    public int getCapacity() { return capacity; }
    public int getScans() { return scans; }
    public int getRemaining() { return remaining; }
    public double getFillRate() { return fillRate; }
}
