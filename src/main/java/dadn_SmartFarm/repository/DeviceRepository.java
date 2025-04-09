package dadn_SmartFarm.repository;

import dadn_SmartFarm.model.Device;
import dadn_SmartFarm.model.FeedInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Query("SELECT d.feedsList FROM Device d")
    List<Map<String, Long>> findAllFeeds();
    List<Device> findByIdNot(long id);

    Device findByFeedsList(Map<String, FeedInfo> feedsList);

    @Query(value = "SELECT COUNT(*) > 0 FROM device WHERE JSON_EXTRACT(feeds_list, CONCAT('$.', :feedKey)) IS NOT NULL", nativeQuery = true)
    long existsFeedKey(@Param("feedKey") String feedKey);

}
