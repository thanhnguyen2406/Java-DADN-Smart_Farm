package dadn_SmartFarm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false, unique = true)
    String roomKey;

    @ElementCollection
    @CollectionTable(name = "room_list_username", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "username")
    @OrderColumn(name = "list_index")
    List<String> listUsername;
}
