package dadn_SmartFarm.dto.LogDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import dadn_SmartFarm.model.enums.LogType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogDTO {
    long id;
    LogType logType;
    String feedKey;
    String value;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Ho_Chi_Minh")
    LocalDateTime createdAt;
}
