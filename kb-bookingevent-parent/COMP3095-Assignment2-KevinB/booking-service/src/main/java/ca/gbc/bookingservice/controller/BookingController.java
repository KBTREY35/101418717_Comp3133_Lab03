package ca.gbc.bookingservice.controller;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest bookingRequest) {
        BookingResponse createdBooking = bookingService.createBooking(bookingRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/booking/" + createdBooking.id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdBooking);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<?> updateBooking(@PathVariable("bookingId") String bookingId,
                                           @RequestBody BookingRequest bookingRequest) {
        String updatedBookingId = bookingService.updateBooking(bookingId, bookingRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/booking/" + updatedBookingId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> deleteBooking(@PathVariable("bookingId") String bookingId) {
        bookingService.deleteBooking(bookingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // New endpoint to check room availability
    @GetMapping("/rooms/{roomId}/availability")
    public ResponseEntity<Boolean> checkRoomAvailability(@PathVariable Long roomId) {
        boolean isAvailable = bookingService.checkAvailability(roomId);
        return ResponseEntity.ok(isAvailable);
    }
}
