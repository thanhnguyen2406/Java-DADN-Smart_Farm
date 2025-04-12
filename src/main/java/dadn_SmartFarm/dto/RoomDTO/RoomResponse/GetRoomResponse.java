package dadn_SmartFarm.dto.RoomDTO.RoomResponse;

import dadn_SmartFarm.dto.RoomDTO.RoomDtoModel.RoomResponseInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetRoomResponse {
    int code;
    String message;
    boolean authenticated;

    List<RoomResponseInfo> listRoom;
}
