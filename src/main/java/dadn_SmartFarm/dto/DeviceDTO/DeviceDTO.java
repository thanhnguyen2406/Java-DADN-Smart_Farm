package dadn_SmartFarm.dto.DeviceDTO;

import dadn_SmartFarm.model.FeedInfo;
import dadn_SmartFarm.model.enums.Status;
import dadn_SmartFarm.model.enums.DeviceType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceDTO {
    long id;
    long roomId;
    String name;
    DeviceType type;
    Status status;
    Map<String, FeedInfo> feedsList;
}
