package dadn_SmartFarm.mapper;

import dadn_SmartFarm.dto.DeviceDTO.DeviceDTO;
import dadn_SmartFarm.model.Device;
import dadn_SmartFarm.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DeviceMapper {

    public Device toDevice (DeviceDTO dto) {
        return Device.builder()
                .roomId(dto.getRoomId())
                .name(dto.getName())
                .type(dto.getType())
                .status(dto.getStatus())
                .feedsList(dto.getFeedsList())
                .build();
    }

    public DeviceDTO toDeviceDTO (Device device) {
        return DeviceDTO.builder()
                .id(device.getId())
                .roomId(device.getRoomId())
                .name(device.getName())
                .type(device.getType())
                .status(device.getStatus())
                .feedsList(device.getFeedsList())
                .build();
    }
}
