package dadn_SmartFarm.model;

import dadn_SmartFarm.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceTrigger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Enumerated(EnumType.STRING)
    Status status;
    String thresholdCondition;
    String sensorFeedKey;
    String controlFeedKey;
}
