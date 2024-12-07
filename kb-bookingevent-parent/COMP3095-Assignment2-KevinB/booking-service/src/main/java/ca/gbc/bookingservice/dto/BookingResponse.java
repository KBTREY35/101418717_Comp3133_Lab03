package ca.gbc.bookingservice.dto;

import java.time.LocalDateTime;

public record BookingResponse(
        String id,
        String userId,
        int roomId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String purpose
) { }
