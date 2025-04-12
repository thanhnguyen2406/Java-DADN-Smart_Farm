package dadn_SmartFarm.mapper;

import dadn_SmartFarm.dto.DeviceDTO.DeviceDTO;
import dadn_SmartFarm.exception.AppException;
import dadn_SmartFarm.exception.ErrorCode;
import dadn_SmartFarm.model.Device;
import dadn_SmartFarm.model.Room;
import dadn_SmartFarm.repository.RoomRepository;
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
