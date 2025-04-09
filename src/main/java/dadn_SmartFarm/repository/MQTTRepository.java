package dadn_SmartFarm.repository;

import dadn_SmartFarm.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MQTTRepository extends JpaRepository<Device, Long> {
}
