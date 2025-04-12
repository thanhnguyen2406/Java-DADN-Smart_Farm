package dadn_SmartHome.dto.RoomDTO.RoomResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRoomResponse {
    int code;
    String message;
    boolean authenticated;

    long roomId;
    String name;
}
