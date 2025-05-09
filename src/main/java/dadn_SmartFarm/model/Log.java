package dadn_SmartFarm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import dadn_SmartFarm.model.enums.LogType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Enumerated(EnumType.STRING)
    LogType logType;

    @Column(nullable = false)
    String feedKey;

    @Column(nullable = true)
    String value;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Ho_Chi_Minh")
    LocalDateTime createdAt;
}
