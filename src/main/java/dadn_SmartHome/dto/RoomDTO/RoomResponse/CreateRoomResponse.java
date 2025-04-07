package dadn_SmartHome.dto.RoomDTO.RoomResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRoomResponse {
    int code;
    String message;
    boolean authenticated;

    long id;
    String name;
}
