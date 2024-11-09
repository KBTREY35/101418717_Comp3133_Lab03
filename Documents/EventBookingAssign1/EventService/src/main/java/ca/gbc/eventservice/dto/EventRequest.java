package ca.gbc.eventservice.dto;

import java.time.LocalDateTime;

public record EventRequest(
        String name,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String location
) {}
