package dadn_SmartHome.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    long userId;

    @Column(nullable = false)
    long deviceId;

    @Column(nullable = false)
    LocalDateTime timestamp;

    @Column(nullable = false)
    String feed_in;

    @Column(nullable = false)
    String feed_out;

    @Column(nullable = false)
    String ioKey;
}
