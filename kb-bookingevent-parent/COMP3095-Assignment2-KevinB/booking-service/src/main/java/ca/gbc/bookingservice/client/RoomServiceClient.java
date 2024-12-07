package ca.gbc.bookingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "room-service", url = "${room.service.url}")
public interface RoomServiceClient {

    @GetMapping("/api/room/{roomId}/availability")
    boolean checkRoomAvailability(@PathVariable("roomId") int roomId);
}

