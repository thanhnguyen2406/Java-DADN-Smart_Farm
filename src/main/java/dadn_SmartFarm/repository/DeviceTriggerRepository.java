package dadn_SmartFarm.repository;

import dadn_SmartFarm.model.DeviceTrigger;
import dadn_SmartFarm.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceTriggerRepository extends JpaRepository<DeviceTrigger, Long> {
    boolean existsBySensorFeedKeyAndControlFeedKeyAndThresholdCondition(
            String sensorFeedKey,
            String controlFeedKey,
            String thresholdCondition
    );

    List<DeviceTrigger> findBySensorFeedKeyAndStatusAndThresholdCondition(
            String sensorFeedKey,
            Status status,
            String thresholdCondition);

    Page<DeviceTrigger> findBySensorFeedKeyIn(List<String> sensorFeedKeys, Pageable pageable);
}
