package dadn_SmartFarm.service.interf;

import dadn_SmartFarm.dto.DeviceTriggerDTO.DeviceTriggerDTO;
import dadn_SmartFarm.dto.Response;
import org.springframework.data.domain.Pageable;

public interface IDeviceTriggerService {
    Response addTrigger(DeviceTriggerDTO deviceTriggerDTO);
    Response deleteTrigger(long id);
    Response updateTrigger(DeviceTriggerDTO deviceTriggerDTO);
    Response getTriggersByDeviceId(Long deviceId, Pageable pageable);
}
