package dadn_SmartHome.service.interf;

import dadn_SmartHome.dto.DeviceDTO.DeviceDTO;
import dadn_SmartHome.dto.Response;

public interface IDeviceService {
    Response addDevice(DeviceDTO request);
    Response updateDevice(DeviceDTO request);
    Response deleteDevice(long id);
    Response encodeDevice(long id);
    Response assignDevice(String encodedFeeds);
}
