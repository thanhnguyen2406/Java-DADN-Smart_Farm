package dadn_SmartHome.dto.ScheduleDTO.ScheduleResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteScheduleResponse {
    int code;
    String message;
    boolean authenticated;
}
