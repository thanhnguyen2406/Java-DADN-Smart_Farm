package dadn_SmartHome.dto.RoomDTO.RoomDtoModel;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomResponseInfo {
    long roomId;
    String roomName;
}
