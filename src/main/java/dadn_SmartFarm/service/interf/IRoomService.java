package dadn_SmartFarm.service.interf;

import dadn_SmartFarm.dto.RoomDTO.RoomRequest.UpdateRoomRequest;
import dadn_SmartFarm.dto.RoomDTO.RoomResponse.CreateRoomResponse;
import dadn_SmartFarm.dto.RoomDTO.RoomResponse.GetRoomResponse;
import dadn_SmartFarm.dto.RoomDTO.RoomResponse.UpdateRoomResponse;


public interface IRoomService {
    GetRoomResponse getRoomResponse();
    CreateRoomResponse createRoom(String roomName);
    UpdateRoomResponse updateRoom(UpdateRoomRequest updateRoomRequest);
}
