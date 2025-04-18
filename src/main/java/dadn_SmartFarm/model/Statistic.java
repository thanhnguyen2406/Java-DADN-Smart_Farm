package dadn_SmartFarm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults (level = AccessLevel.PRIVATE)
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotNull
    long feedId;

    @NotNull
    double value;

    @NotNull
    @Column(name = "timestamp", columnDefinition = "DATETIME")
    LocalDateTime timeStamp;
}
