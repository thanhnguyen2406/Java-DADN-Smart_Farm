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

    @Query("SELECT CASE WHEN COUNT(r) > 0 " +
            "THEN true ELSE false END " +
            "FROM Room r " +
            "WHERE r.id = :roomId " +
            "AND r.email = :email ")
    boolean existsByRoomIdAndEmail(@Param("roomId") Long roomId, @Param("email") String email);

    @Query("select case when count(r) > 0 " +
            "then true else false end " +
            "from Room r " +
            "where r.email = :email " +
            "AND r.name = :roomName ")
    Boolean existByEmailAndName(@Param("email") String email, @Param("roomName") String roomName);

    List<Room> getRoomsByEmail(String email);

    Optional<Room> findByIdAndEmail(Long id, String email);
}
