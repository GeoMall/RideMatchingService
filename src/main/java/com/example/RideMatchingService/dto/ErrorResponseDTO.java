package com.example.RideMatchingService.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ErrorResponseDTO {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponseDTO(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now(); // always set automatically
    }
}