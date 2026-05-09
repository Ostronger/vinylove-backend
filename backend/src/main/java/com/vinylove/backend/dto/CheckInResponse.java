package com.vinylove.backend.dto;

import java.time.LocalDateTime;

public class CheckInResponse {

    private String message;
    private Long guestId;
    private String guestFullName;
    private Long eventId;
    private String eventName;
    private boolean checkedIn;
    private LocalDateTime checkedInAt;

    public CheckInResponse(String message, Long guestId, String guestFullName,
                           Long eventId, String eventName, boolean checkedIn,
                           LocalDateTime checkedInAt) {
        this.message = message;
        this.guestId = guestId;
        this.guestFullName = guestFullName;
        this.eventId = eventId;
        this.eventName = eventName;
        this.checkedIn = checkedIn;
        this.checkedInAt = checkedInAt;
    }

    public String getMessage() { return message; }
    public Long getGuestId() { return guestId; }
    public String getGuestFullName() { return guestFullName; }
    public Long getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public boolean isCheckedIn() { return checkedIn; }
    public LocalDateTime getCheckedInAt() { return checkedInAt; }
}
