package ca.gbc.roomservice.service;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;

import java.util.List;

public interface RoomService {
    RoomResponse createRoom(RoomRequest roomRequest);
    List<RoomResponse> getAllRooms();
    boolean checkAvailability(Long roomId);
    Long updateRoom(Long roomId, RoomRequest roomRequest);
    void deleteRoom(Long roomId);
}
