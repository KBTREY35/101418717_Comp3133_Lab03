package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    // Create a booking
    BookingResponse createBooking(BookingRequest bookingRequest);

    // Get all bookings
    List<BookingResponse> getAllBookings();

    // Update a booking
    String updateBooking(String bookingId, BookingRequest bookingRequest);

    // Delete a booking
    void deleteBooking(String bookingId);

    // Check availability for a room
    boolean checkAvailability(int roomId, LocalDateTime startTime, LocalDateTime endTime);

    boolean checkAvailability(Long roomId);
}
