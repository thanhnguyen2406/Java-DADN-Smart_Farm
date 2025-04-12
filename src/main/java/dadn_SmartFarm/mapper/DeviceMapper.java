package dadn_SmartFarm.mapper;

import dadn_SmartFarm.dto.DeviceDTO.DeviceDTO;
import dadn_SmartFarm.model.Device;
import dadn_SmartFarm.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DeviceMapper {

    private final RoomRepository roomRepository;

    public Device toDevice (DeviceDTO dto) {
        return Device.builder()
                .roomId(dto.getRoomId())
                .name(dto.getName())
                .type(dto.getType())
                .status(dto.getStatus())
                .feedsList(dto.getFeedsList())
                .build();
    }
}
