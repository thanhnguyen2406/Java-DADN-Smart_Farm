package dadn_SmartFarm.controller;

import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.dto.RoomDTO.RoomRequest.CreateRoomRequest;
import dadn_SmartFarm.dto.RoomDTO.RoomRequest.UpdateRoomRequest;
import dadn_SmartFarm.dto.RoomDTO.RoomResponse.CreateRoomResponse;
import dadn_SmartFarm.dto.RoomDTO.RoomResponse.GetRoomResponse;
import dadn_SmartFarm.dto.RoomDTO.RoomResponse.UpdateRoomResponse;
import dadn_SmartFarm.service.implement.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/add")
    public CreateRoomResponse createRoom(@RequestBody CreateRoomRequest request) {
        return roomService.createRoom(request.getName());
    }

    @GetMapping()
    public GetRoomResponse getRoom() {
        return roomService.getRoomResponse();
    }

    @PutMapping("/update")
    public UpdateRoomResponse updateRoom(@RequestBody UpdateRoomRequest request) {
        return roomService.updateRoom(request);
    }

    @GetMapping("/encode/{id}")
    public ResponseEntity<Response> encodeRoom(@PathVariable("id") long id) {
        Response response = roomService.encodeRoom(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/assign")
    public ResponseEntity<Response> assignRoom(@RequestParam String encodedRoom) {
        Response response = roomService.assignRoom(encodedRoom);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}