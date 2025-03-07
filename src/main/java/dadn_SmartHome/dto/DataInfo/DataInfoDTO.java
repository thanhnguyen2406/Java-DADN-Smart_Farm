package dadn_SmartHome.dto.DataInfo;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataInfoDTO {
    String value;
    long feed_id;
    String feed_key;
}
