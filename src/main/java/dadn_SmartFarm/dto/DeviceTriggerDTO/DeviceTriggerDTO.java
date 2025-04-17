package dadn_SmartFarm.dto.DeviceTriggerDTO;

import dadn_SmartFarm.model.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceTriggerDTO {
    long id;
    Status status;
    @NotNull(message = "Please enter value that needed to be send")
    String valueSend;
    @NotNull(message = "Please enter condition to trigger the feed")
    String condition;
    @NotNull(message = "Please enter sensor feed key")
    String sensorFeedKey;
    @NotNull(message = "Please enter control feed key")
    String controlFeedKey;
}
