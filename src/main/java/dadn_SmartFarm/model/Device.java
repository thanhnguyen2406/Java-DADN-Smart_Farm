package dadn_SmartFarm.model;

import dadn_SmartFarm.model.enums.Status;
import dadn_SmartFarm.model.enums.DeviceType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    String name;

    String userEmail;

    @Column(nullable = false)
    Status status;

    @Column(nullable = false)
    DeviceType type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json", nullable = false, unique = true)
    Map<String, FeedInfo> feedsList;
}
