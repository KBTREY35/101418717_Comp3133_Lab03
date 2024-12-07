package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.client.RoomServiceClient;
import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.repository.BookingRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final MongoTemplate mongoTemplate;
    private final RoomServiceClient roomServiceClient;

    @Override
    @CircuitBreaker(name = "roomService", fallbackMethod = "fallbackCheckRoomAvailability")
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        log.info("Checking availability for room {}", bookingRequest.roomId());

        // Check if the room is available
        boolean isRoomAvailable = roomServiceClient.checkRoomAvailability(bookingRequest.roomId());
        if (!isRoomAvailable) {
            throw new RuntimeException("Room " + bookingRequest.roomId() + " is not available for booking");
        }

        Booking booking = Booking.builder()
                .userId(bookingRequest.userId())
                .roomId(bookingRequest.roomId())
                .startTime(bookingRequest.startTime())
                .endTime(bookingRequest.endTime())
                .purpose(bookingRequest.purpose())
                .build();

        booking = bookingRepository.save(booking);
        log.info("Booking {} is saved", booking.getId());

        return mapToBookingResponse(booking);
    }

    // Fallback method for createBooking when RoomService is unavailable
    public BookingResponse fallbackCheckRoomAvailability(BookingRequest bookingRequest, Throwable throwable) {
        log.error("Fallback executed for RoomService due to: {}", throwable.getMessage());
        throw new RuntimeException("Unable to check room availability at this time. Please try again later.");
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        log.debug("Retrieving all bookings");

        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(this::mapToBookingResponse).toList();
    }

    private BookingResponse mapToBookingResponse(Booking booking) {
        return new BookingResponse(booking.getId(), booking.getUserId(), booking.getRoomId(), booking.getStartTime(), booking.getEndTime(), booking.getPurpose()
        );
    }

    @Override
    public String updateBooking(String bookingId, BookingRequest bookingRequest) {
        log.debug("Updating booking with ID {}", bookingId);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(bookingId));
        Booking booking = mongoTemplate.findOne(query, Booking.class);

        if (booking == null) {
            return null;
        }

        booking.setUserId(bookingRequest.userId());
        booking.setRoomId(bookingRequest.roomId());
        booking.setStartTime(bookingRequest.startTime());
        booking.setEndTime(bookingRequest.endTime());
        booking.setPurpose(bookingRequest.purpose());

        bookingRepository.save(booking);

        return booking.getId();
    }

    @Override
    public boolean checkAvailability(int roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> overlappingBookings = bookingRepository.findByRoomIdAndStartTimeBeforeAndEndTimeAfter(
                roomId, endTime, startTime
        );
        return overlappingBookings.isEmpty();
    }

    @Override
    public boolean checkAvailability(Long roomId) {
        return false;
    }

    @Override
    public void deleteBooking(String bookingId) {
        log.debug("Deleting booking with ID {}", bookingId);
        bookingRepository.deleteById(bookingId);
    }
}
