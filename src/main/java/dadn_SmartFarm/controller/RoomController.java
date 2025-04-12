package dadn_SmartHome.controller;

import dadn_SmartHome.dto.RoomDTO.RoomRequest.CreateRoomRequest;
import dadn_SmartHome.dto.RoomDTO.RoomRequest.UpdateRoomRequest;
import dadn_SmartHome.dto.RoomDTO.RoomResponse.CreateRoomResponse;
import dadn_SmartHome.dto.RoomDTO.RoomResponse.GetRoomResponse;
import dadn_SmartHome.dto.RoomDTO.RoomResponse.UpdateRoomResponse;
import dadn_SmartHome.service.implement.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    @Autowired
    private final RoomService roomService;

    @PostMapping()
    public CreateRoomResponse createRoom(@RequestBody CreateRoomRequest request) {
        return roomService.createRoom(request.getName());
    }

    @GetMapping()
    public GetRoomResponse getRoom() {
        return roomService.getRoomResponse();
    }

    @PutMapping("/update-name")
    public UpdateRoomResponse updateRoom(@RequestBody UpdateRoomRequest request) {
        return roomService.updateRoom(request);
    }

}
