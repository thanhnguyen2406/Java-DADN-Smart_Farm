package dadn_SmartHome.model;

import dadn_SmartHome.model.enums.DeviceStatus;
import dadn_SmartHome.model.enums.DeviceType;
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

    @ManyToOne
    @JoinColumn(nullable = false)
    Room room;

    @Column(nullable = false)
    String name;

    String userEmail;

    @Column(nullable = false)
    DeviceStatus status;

    @Column(nullable = false)
    DeviceType type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json", nullable = false, unique = true)
    Map<String, FeedInfo> feedsList;
}
