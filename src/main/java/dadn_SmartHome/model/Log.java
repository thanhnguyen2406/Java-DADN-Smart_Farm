package dadn_SmartHome.model;

import dadn_SmartHome.model.enums.LogType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
