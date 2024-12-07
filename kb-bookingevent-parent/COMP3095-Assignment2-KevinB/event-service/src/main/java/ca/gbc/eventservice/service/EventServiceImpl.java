package ca.gbc.eventservice.service;

import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.dto.EventResponse;
import ca.gbc.eventservice.model.Event;
import ca.gbc.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public EventResponse createEvent(EventRequest eventRequest) {
        log.debug("Creating a new event {}", eventRequest.name());

        Event event = Event.builder()
                .name(eventRequest.name())
                .description(eventRequest.description())
                .startTime(eventRequest.startTime())
                .endTime(eventRequest.endTime())
                .location(eventRequest.location())
                .build();

        Event savedEvent = eventRepository.save(event);

        log.info("Event {} is saved", savedEvent.getId());

        return mapToEventResponse(savedEvent);
    }

    private EventResponse mapToEventResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getStartTime(),
                event.getEndTime(),
                event.getLocation()
        );
    }

    @Override
    public List<EventResponse> getAllEvents() {
        log.debug("Getting all events");

        List<Event> events = eventRepository.findAll();
        return events.stream().map(this::mapToEventResponse).collect(Collectors.toList());
    }

    @Override
    public EventResponse getEventById(String eventId) {
        log.debug("Getting event by ID {}", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return mapToEventResponse(event);
    }

    @Override
    public void updateEvent(String eventId, EventRequest eventRequest) {
        log.debug("Updating event {}", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setName(eventRequest.name());
        event.setDescription(eventRequest.description());
        event.setStartTime(eventRequest.startTime());
        event.setEndTime(eventRequest.endTime());
        event.setLocation(eventRequest.location());
        eventRepository.save(event);
    }

    @Override
    public void deleteEvent(String eventId) {
        log.debug("Deleting event {}", eventId);
        eventRepository.deleteById(eventId);
    }
}
