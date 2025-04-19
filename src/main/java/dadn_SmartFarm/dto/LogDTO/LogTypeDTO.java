package dadn_SmartFarm.dto.LogDTO;

import dadn_SmartFarm.model.enums.LogType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogTypeDTO {
    LogType logType;
    String feedKey;
}
