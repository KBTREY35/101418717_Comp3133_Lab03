package ca.gbc.roomservice.controller;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;
import ca.gbc.roomservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest roomRequest) {
        RoomResponse createdRoom = roomService.createRoom(roomRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/room/" + createdRoom.id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdRoom);
    }

    // READ
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    // CHECK AVAILABILITY
    @GetMapping("/{roomId}/availability")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable("roomId") Long roomId) {
        return ResponseEntity.ok(roomService.checkAvailability(roomId));
    }

    // UPDATE
    @PutMapping("/{roomId}")
    public ResponseEntity<Void> updateRoom(@PathVariable("roomId") Long roomId,
                                           @RequestBody RoomRequest roomRequest) {

        Long updatedRoomId = roomService.updateRoom(roomId, roomRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/room/" + updatedRoomId);

        return ResponseEntity.noContent().headers(headers).build();
    }

    // DELETE
    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable("roomId") Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
