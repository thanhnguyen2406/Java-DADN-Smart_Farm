package dadn_SmartHome.dto.DataInfo;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataDTO {
    @NotBlank
    String url;

    String value;
    long feed_id;
    String feed_key;
}
