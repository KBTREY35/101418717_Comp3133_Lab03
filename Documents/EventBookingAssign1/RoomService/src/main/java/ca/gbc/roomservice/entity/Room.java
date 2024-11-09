package ca.gbc.roomservice.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;
    private int capacity;
    private boolean availability;

    @ElementCollection
    private List<String> features;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getRoomName() { return roomName; }

    public void setRoomName(String roomName) { this.roomName = roomName; }

    public int getCapacity() { return capacity; }

    public void setCapacity(int capacity) { this.capacity = capacity; }

    public boolean isAvailability() { return availability; }

    public void setAvailability(boolean availability) { this.availability = availability; }

    public List<String> getFeatures() { return features; }

    public void setFeatures(List<String> features) { this.features = features; }
}
