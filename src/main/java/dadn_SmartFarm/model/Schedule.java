package dadn_SmartFarm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import dadn_SmartFarm.model.enums.ScheduleType;
import dadn_SmartFarm.model.enums.Status;
import dadn_SmartFarm.model.enums.WeekDay;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    Device device ;

    @NotNull
    long feedId;

    @NotNull
    @Enumerated(EnumType.STRING)
    Status status;

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
