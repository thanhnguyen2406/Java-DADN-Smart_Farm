package dadn_SmartFarm.dto.DeviceDTO;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceRoomDTO {
    long roomId;
    long deviceId;
}
