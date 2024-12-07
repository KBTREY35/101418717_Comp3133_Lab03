package ca.gbc.eventservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Document(collection = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @Id
    private String id;

    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
}

