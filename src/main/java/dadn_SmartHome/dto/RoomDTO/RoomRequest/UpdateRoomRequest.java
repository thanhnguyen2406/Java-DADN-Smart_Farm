package dadn_SmartHome.dto.RoomDTO.RoomRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRoomRequest {
    long id;
    String name;
}