package dadn_SmartHome.repository;

import dadn_SmartHome.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MQTTRepository extends JpaRepository<Device, Long> {
}
