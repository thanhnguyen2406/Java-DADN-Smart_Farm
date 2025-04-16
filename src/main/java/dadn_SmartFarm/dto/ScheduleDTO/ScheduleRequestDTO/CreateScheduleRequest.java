package dadn_SmartFarm.dto.ScheduleDTO.ScheduleRequestDTO;

import dadn_SmartFarm.model.enums.Status;
import dadn_SmartFarm.model.enums.ScheduleType;
import dadn_SmartFarm.model.enums.WeekDay;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateScheduleRequest {
    @NotNull
    long id_device ;

    @NotNull
    long feedId;

    Status status;
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
