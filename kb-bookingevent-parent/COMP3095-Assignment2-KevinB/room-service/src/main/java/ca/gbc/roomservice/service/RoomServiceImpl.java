package ca.gbc.roomservice.service;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;
import ca.gbc.roomservice.model.Room;
import ca.gbc.roomservice.repository.RoomRepository;
import ca.gbc.roomservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public RoomResponse createRoom(RoomRequest roomRequest) {
        log.debug("Creating a new room {}", roomRequest.roomName());

        Room room = Room.builder()
                .roomName(roomRequest.roomName())
                .capacity(roomRequest.capacity())
                .features(roomRequest.features())
                .available(roomRequest.available())
                .build();

        Room savedRoom = roomRepository.save(room);

        log.info("Room {} is saved", savedRoom.getId());

        return mapToRoomResponse(savedRoom);
    }

    private RoomResponse mapToRoomResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getRoomName(),
                room.getCapacity(),
                room.getFeatures(),
                room.getAvailable()
        );
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        log.debug("Getting all rooms");

        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(this::mapToRoomResponse).toList();
    }

    @Override
    public boolean checkAvailability(Long roomId) {
        log.debug("Checking availability for room {}", roomId);
        return roomRepository.findById(roomId)
                .map(Room::getAvailable)
                .orElse(false);
    }

    @Override
    public Long updateRoom(Long roomId, RoomRequest roomRequest) {
        log.debug("Updating room {}", roomId);

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        room.setRoomName(roomRequest.roomName());
        room.setCapacity(roomRequest.capacity());
        room.setFeatures(roomRequest.features());
        room.setAvailable(roomRequest.available());
        roomRepository.save(room);

        return room.getId();
    }

    @Override
    public void deleteRoom(Long roomId) {
        log.debug("Deleting room {}", roomId);
        roomRepository.deleteById(roomId);
    }

}
