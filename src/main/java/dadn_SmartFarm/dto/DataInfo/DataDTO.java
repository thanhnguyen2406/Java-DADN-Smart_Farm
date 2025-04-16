package dadn_SmartFarm.dto.DataInfo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataDTO {
    String value;
    long feed_id;
    String feed_key;
}
