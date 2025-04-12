package dadn_SmartFarm.mapper;

import dadn_SmartFarm.dto.RoomDTO.RoomDtoModel.RoomResponseInfo;
import dadn_SmartFarm.model.Room;
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
