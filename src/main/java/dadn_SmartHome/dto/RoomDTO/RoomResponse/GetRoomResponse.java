package dadn_SmartHome.dto.RoomDTO.RoomResponse;

import dadn_SmartHome.dto.RoomDTO.RoomDtoModel.RoomResponseInfo;
import lombok.*;
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
