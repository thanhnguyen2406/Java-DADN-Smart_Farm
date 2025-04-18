package dadn_SmartFarm.dto.DeviceDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceDTO {
    long id;
    Long roomId;
    String name;
    DeviceType type;
    Status status;
    Map<String, FeedInfo> feedsList;
}
