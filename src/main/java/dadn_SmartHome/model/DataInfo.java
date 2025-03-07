package dadn_SmartHome.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataInfo {
    String id;
    String value;

    @JsonProperty("feed_id")
    long feedId;

    @JsonProperty("feed_key")
    String feedKey;

    @JsonProperty("created_at")
    String createdAt;

    @JsonProperty("created_epoch")
    long createdEpoch;

    String expiration;
}

