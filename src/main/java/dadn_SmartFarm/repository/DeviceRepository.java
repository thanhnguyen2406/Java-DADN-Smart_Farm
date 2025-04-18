package dadn_SmartFarm.repository;

import dadn_SmartFarm.model.Device;
import dadn_SmartFarm.model.enums.DeviceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Query("SELECT d.feedsList FROM Device d")
    List<Map<String, Long>> findAllFeeds();
    List<Device> findByIdNot(long id);

    @Query("SELECT d.id from Device d where d.roomId = :roomId")
    Optional<List<Long>> findIdByRoomId(@Param("roomId") long roomId);

//    Device findByFeedsList(Map<String, FeedInfo> feedsList);

    @Query(value = "SELECT COUNT(*) > 0 FROM device WHERE JSON_EXTRACT(feeds_list, CONCAT('$.', :feedKey)) IS NOT NULL", nativeQuery = true)
    long existsFeedKey(@Param("feedKey") String feedKey);

    @Query(value = """
    SELECT COUNT(*) > 0
    FROM device
    WHERE type = 'SENSOR'
    AND JSON_EXTRACT(feeds_list, CONCAT('$.', :sensorKey)) IS NOT NULL
    """, nativeQuery = true)
    long existsSensorTriggerFeedKey(@Param("sensorKey") String sensorKey);


    @Query(value = """
    SELECT COUNT(*) > 0 
    FROM device
    WHERE type = 'CONTROL'
    AND JSON_EXTRACT(feeds_list, CONCAT('$.', :controlKey)) IS NOT NULL
    """, nativeQuery = true)
    long existsControlFeedKey(@Param("controlKey") String controlKey);
    @Query(value = """
    SELECT * FROM device 
    JOIN JSON_TABLE(
      JSON_EXTRACT(feeds_list, '$'),
      '$.*' COLUMNS (
        feedId BIGINT PATH '$.feedId'
      )
    ) AS jt
    ON jt.feedId = :feedId
    LIMIT 1
    """, nativeQuery = true)
    Optional<Device> findDeviceByFeedIdInJson(@Param("feedId") Long feedId);

    List<Device> findByType(DeviceType type);

}
