package com.vinylove.backend.dto;

public class TableCheckInResponse {

    private String message;
    private Long tableId;
    private String label;
    private int capacity;
    private int scanCount;
    private int remainingEntries;
    private boolean accepted;

    public TableCheckInResponse(
            String message,
            Long tableId,
            String label,
            int capacity,
            int scanCount,
            int remainingEntries,
            boolean accepted
    ) {
        this.message = message;
        this.tableId = tableId;
        this.label = label;
        this.capacity = capacity;
        this.scanCount = scanCount;
        this.remainingEntries = remainingEntries;
        this.accepted = accepted;
    }

    public String getMessage() {
        return message;
    }

    public Long getTableId() {
        return tableId;
    }

    public String getLabel() {
        return label;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getScanCount() {
        return scanCount;
    }

    public int getRemainingEntries() {
        return remainingEntries;
    }

    public boolean isAccepted() {
        return accepted;
    }
}
