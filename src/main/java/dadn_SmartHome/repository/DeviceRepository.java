package dadn_SmartHome.repository;

import dadn_SmartHome.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Query("SELECT d.feedsList FROM Device d")
    List<Map<String, Long>> findAllFeeds();
    List<Device> findByIdNot(long id);

    @Query("SELECT d.id from Device d where d.room.id = :roomId")
    Optional<List<Long>> findIdByRoomId(@Param("roomId") long roomId);

    Device findByFeedsList(Map<String, Long> feedsList);
}
