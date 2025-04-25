package dadn_SmartFarm.dto.DeviceTriggerDTO;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedDTO {
    String feedKey;
    String deviceName;
}
