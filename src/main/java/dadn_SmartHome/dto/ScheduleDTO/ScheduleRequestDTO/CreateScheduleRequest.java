package dadn_SmartHome.dto.ScheduleDTO.ScheduleRequestDTO;

import dadn_SmartHome.model.enums.DeviceStatus;
import dadn_SmartHome.model.enums.ScheduleType;
import dadn_SmartHome.model.enums.WeekDay;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateScheduleRequest {
    long id_device ;

    DeviceStatus status;
    String description;

    @Enumerated(EnumType.STRING)
    ScheduleType scheduleType;

    @Enumerated(EnumType.STRING)
    WeekDay weekDay;

    @FutureOrPresent(message = "Ngày bắt đầu phải là tương lai")
    LocalDate startDate;
    LocalDate endDate;

    @NotNull
    LocalTime time_from;
    LocalTime time_to;
}
