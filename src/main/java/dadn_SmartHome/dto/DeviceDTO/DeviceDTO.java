package dadn_SmartHome.dto.DeviceDTO;

import dadn_SmartHome.model.FeedInfo;
import dadn_SmartHome.model.enums.DeviceStatus;
import dadn_SmartHome.model.enums.DeviceType;
import jakarta.validation.constraints.NotNull;
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
    DeviceStatus status;
    Map<String, FeedInfo> feedsList;
}
