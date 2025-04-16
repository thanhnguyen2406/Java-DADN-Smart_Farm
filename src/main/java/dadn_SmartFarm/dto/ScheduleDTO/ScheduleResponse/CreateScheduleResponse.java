package dadn_SmartFarm.dto.ScheduleDTO.ScheduleResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import dadn_SmartFarm.model.enums.Status;
import dadn_SmartFarm.model.enums.ScheduleType;
import dadn_SmartFarm.model.enums.WeekDay;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateScheduleResponse {
    int code;
    String message;
    boolean authenticated;

    long id_device;
    long feedId;
    Status status;
    String description;

    @Enumerated(EnumType.STRING)
    ScheduleType scheduleType;

    @Enumerated(EnumType.STRING)
    WeekDay weekDay;

    LocalDate startDate;
    LocalDate endDate;

    LocalTime time_from;
    LocalTime time_to;
}
