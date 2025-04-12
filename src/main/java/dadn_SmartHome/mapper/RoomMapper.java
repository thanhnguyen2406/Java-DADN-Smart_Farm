package dadn_SmartHome.mapper;

import dadn_SmartHome.dto.RoomDTO.RoomDtoModel.RoomResponseInfo;
import dadn_SmartHome.dto.RoomDTO.RoomResponse.GetRoomResponse;
import dadn_SmartHome.model.Room;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Builder
@Component
public class RoomMapper {
    public RoomResponseInfo toRoomResponseInfo(Room room) {
        return RoomResponseInfo.builder()
                .roomId(room.getId())
                .roomName(room.getName())
                .build();
    }
}
