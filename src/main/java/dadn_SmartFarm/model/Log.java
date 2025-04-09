package dadn_SmartFarm.model;

import dadn_SmartFarm.model.enums.LogType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Log {
    @Id
    long id;

    LogType logType;

    @Column(nullable = false)
    long device_id;

    @Column(nullable = false)
    String message;

    @Column(nullable = true)
    String value;
}
