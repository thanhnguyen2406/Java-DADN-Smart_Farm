package dadn_SmartFarm.repository;

import dadn_SmartFarm.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM Room r JOIN r.listUsername u " +
            "WHERE r.id = :roomId AND u = :email")
    boolean existsByRoomIdAndEmail(@Param("roomId") Long roomId, @Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Room r JOIN r.listUsername u " +
            "WHERE u = :email AND r.name = :roomName")
    Boolean existsByEmailAndName(@Param("email") String email, @Param("roomName") String roomName);

    @Query("SELECT r FROM Room r JOIN r.listUsername u WHERE u = :email")
    List<Room> getRoomsByUsername(@Param("email") String email);

    @Query("SELECT r FROM Room r JOIN r.listUsername u " +
            "WHERE r.id = :id AND u = :email")
    Optional<Room> findByIdAndUsername(@Param("id") Long id, @Param("email") String email);
}
