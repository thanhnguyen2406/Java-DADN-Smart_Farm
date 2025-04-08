package dadn_SmartHome.mapper;

import dadn_SmartHome.dto.DeviceDTO.DeviceDTO;
import dadn_SmartHome.model.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {
    public Device toDevice (DeviceDTO dto) {
        return Device.builder()
                .name(dto.getName())
                .type(dto.getType())
                .status(dto.getStatus())
                .feedsList(dto.getFeedsList())
                .build();
    }
}
