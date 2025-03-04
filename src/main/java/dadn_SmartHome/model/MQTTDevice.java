package dadn_SmartHome.model;

import dadn_SmartHome.model.enums.DeviceStatus;
import dadn_SmartHome.model.enums.DeviceType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MQTTDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    DeviceType type;

    @Column(nullable = false)
    DeviceStatus status;
}
