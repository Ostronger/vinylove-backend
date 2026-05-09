package com.vinylove.backend.dto;

public class CheckInRequest {
    
    private String qrCode;

    public CheckInRequest() {
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
