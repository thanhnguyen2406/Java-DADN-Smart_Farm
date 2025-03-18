package dadn_SmartHome.dto.DeviceDTO;

import dadn_SmartHome.model.enums.DeviceStatus;
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
    Long id;
    String name;
    DeviceStatus status;
    Map<String, Long> feedsList;
}
