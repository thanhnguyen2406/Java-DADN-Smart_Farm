package dadn_SmartFarm.mapper;

import dadn_SmartHome.dto.DeviceDTO.DeviceDTO;
import dadn_SmartHome.exception.AppException;
import dadn_SmartHome.exception.ErrorCode;
import dadn_SmartHome.model.Device;
import dadn_SmartHome.model.Room;
import dadn_SmartHome.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DeviceMapper {

    private final RoomRepository roomRepository;

    public Device toDevice (DeviceDTO dto) {
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        return Device.builder()
                .room(room)
                .name(dto.getName())
                .type(dto.getType())
                .status(dto.getStatus())
                .feedsList(dto.getFeedsList())
                .build();
    }
}
