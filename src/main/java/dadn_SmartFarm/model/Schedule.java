package dadn_SmartHome.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import dadn_SmartHome.model.enums.DeviceStatus;
import dadn_SmartHome.model.enums.ScheduleType;
import dadn_SmartHome.model.enums.WeekDay;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalTime;
import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    Device device ;

    @NotNull
    @Enumerated(EnumType.STRING)
    DeviceStatus status;

    String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    ScheduleType scheduleType;

    @Enumerated(EnumType.STRING)
    WeekDay weekDay;

    LocalDate startDate;
    LocalDate endDate;

    @NotNull
    LocalTime time_from;
    LocalTime time_to;
}
