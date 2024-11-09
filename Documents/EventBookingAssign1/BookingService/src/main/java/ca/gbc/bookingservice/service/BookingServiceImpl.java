package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        log.debug("Creating a new booking {}", bookingRequest.getPurpose());

        Booking booking = Booking.builder()
                .userId(bookingRequest.getUserId())
                .roomId(bookingRequest.getRoomId())
                .startTime(bookingRequest.getStartTime())
                .endTime(bookingRequest.getEndTime())
                .purpose(bookingRequest.getPurpose())
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        log.info("Booking {} is saved", savedBooking.getId());

        return mapToBookingResponse(savedBooking);
    }

    private BookingResponse mapToBookingResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUserId(),
                booking.getRoomId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getPurpose()
        );
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        log.debug("Getting all bookings");

        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(this::mapToBookingResponse).toList();
    }

    @Override
    public BookingResponse getBookingById(String bookingId) {
        log.debug("Fetching booking with ID {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return mapToBookingResponse(booking);
    }

    @Override
    public void updateBooking(String bookingId, BookingRequest bookingRequest) {
        log.debug("Updating booking {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setUserId(bookingRequest.getUserId());
        booking.setRoomId(bookingRequest.getRoomId());
        booking.setStartTime(bookingRequest.getStartTime());
        booking.setEndTime(bookingRequest.getEndTime());
        booking.setPurpose(bookingRequest.getPurpose());

        bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(String bookingId) {
        log.debug("Deleting booking {}", bookingId);
        bookingRepository.deleteById(bookingId);
    }
}
