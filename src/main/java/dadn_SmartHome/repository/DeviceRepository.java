package dadn_SmartHome.repository;

import dadn_SmartHome.model.Device;
import dadn_SmartHome.model.FeedInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Query("SELECT d.feedsList FROM Device d")
    List<Map<String, Long>> findAllFeeds();
    List<Device> findByIdNot(long id);

    Device findByFeedsList(Map<String, FeedInfo> feedsList);
}
