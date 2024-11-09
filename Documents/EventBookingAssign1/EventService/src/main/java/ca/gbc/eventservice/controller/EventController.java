package ca.gbc.eventservice.controller;

import ca.gbc.eventservice.entity.Event;
import ca.gbc.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        String userId = event.getOrganizerId();
        ResponseEntity<String> userResponse = restTemplate.getForEntity("http://localhost:8080/api/users/" + userId, String.class);

        if (userResponse.getStatusCode().is2xxSuccessful()) {
            String userRole = userResponse.getBody();

            if (isValidRole(userRole)) {
                Event createdEvent = eventService.createEvent(event);
                return ResponseEntity.ok(createdEvent);
            } else {
                return ResponseEntity.status(403).build();
            }
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable String id) {
        Event event = eventService.getEventById(id);
        return event == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable String id, @RequestBody Event event) {
        event.setId(id);
        Event updatedEvent = eventService.updateEvent(event);
        return updatedEvent != null ? ResponseEntity.ok(updatedEvent) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isValidRole(String role) {
        // Implement your role validation logic
        return role.equals("faculty");
    }
}
