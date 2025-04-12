package dadn_SmartHome.service.interf;

import dadn_SmartHome.dto.RoomDTO.RoomRequest.UpdateRoomRequest;
import dadn_SmartHome.dto.RoomDTO.RoomResponse.CreateRoomResponse;
import dadn_SmartHome.dto.RoomDTO.RoomResponse.GetRoomResponse;
import dadn_SmartHome.dto.RoomDTO.RoomResponse.UpdateRoomResponse;


public interface IRoomService {
    GetRoomResponse getRoomResponse();
    CreateRoomResponse createRoom(String roomName);
    UpdateRoomResponse updateRoom(UpdateRoomRequest updateRoomRequest);
}
