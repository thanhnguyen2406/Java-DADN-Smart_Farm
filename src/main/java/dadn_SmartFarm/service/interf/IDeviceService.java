package dadn_SmartFarm.service.interf;

import dadn_SmartFarm.dto.DeviceDTO.DeviceDTO;
import dadn_SmartFarm.dto.Response;

public interface IDeviceService {
    Response addDevice(DeviceDTO request);
    Response updateDevice(DeviceDTO request);
    Response deleteDevice(long id);
//    Response encodeDevice(long id);
//    Response assignDevice(String encodedFeeds);
}
