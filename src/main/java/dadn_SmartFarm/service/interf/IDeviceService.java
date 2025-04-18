package dadn_SmartFarm.service.interf;

import dadn_SmartFarm.dto.DeviceDTO.DeviceDTO;
import dadn_SmartFarm.dto.DeviceDTO.DeviceRoomDTO;
import dadn_SmartFarm.dto.Response;
import org.springframework.data.domain.Pageable;

public interface IDeviceService {
    Response addDevice(DeviceDTO request);
    Response updateDevice(DeviceDTO request);
    Response deleteDevice(long id);
    Response assignDeviceToRoom(DeviceRoomDTO request);
    Response dismissDeviceToRoom(long id);
    Response getDevicesByRoomId(long id, Pageable pageable);
    Response getUnassignedDevices(Pageable pageable);
}
