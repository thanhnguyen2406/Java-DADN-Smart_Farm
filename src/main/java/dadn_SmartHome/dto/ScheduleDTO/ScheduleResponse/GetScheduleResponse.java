package dadn_SmartHome.dto.ScheduleDTO.ScheduleResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import dadn_SmartHome.model.Schedule;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetScheduleResponse {
    int code;
    String message;
    String token;
    boolean authenticated;

    List<Schedule> schedules;
}
