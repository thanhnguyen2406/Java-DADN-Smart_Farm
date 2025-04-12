package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.dto.RoomDTO.RoomDtoModel.RoomResponseInfo;
import dadn_SmartFarm.dto.RoomDTO.RoomRequest.UpdateRoomRequest;
import dadn_SmartFarm.dto.RoomDTO.RoomResponse.CreateRoomResponse;
import dadn_SmartFarm.dto.RoomDTO.RoomResponse.GetRoomResponse;
import dadn_SmartFarm.dto.RoomDTO.RoomResponse.UpdateRoomResponse;
import dadn_SmartFarm.exception.AppException;
import dadn_SmartFarm.exception.ErrorCode;
import dadn_SmartFarm.mapper.RoomMapper;
import dadn_SmartFarm.model.Room;
import dadn_SmartFarm.repository.RoomRepository;
import dadn_SmartFarm.service.interf.IRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService implements IRoomService {

    @Autowired
    private final RoomRepository roomRepository;

    @Autowired
    private final RoomMapper roomMapper;

    @Override
    public CreateRoomResponse createRoom(String roomName) {

        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        log.info(email);

        if(roomRepository.existByEmailAndName(email, roomName)) {
            throw new AppException(ErrorCode.ROOM_EXISTED);
        }

        Room room = new Room();
        room.setName(roomName);
        room.setEmail(email);
        roomRepository.save(room);

        return CreateRoomResponse.builder()
                .code(200)
                .message("Success")
                .authenticated(true)
                .id(room.getId())
                .name(roomName)
                .build();
    }

    @Override
    public GetRoomResponse getRoomResponse() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        List<Room> listRoomInfo = roomRepository.getRoomsByEmail(email);

        List<RoomResponseInfo> roomResponseInfo = new ArrayList<>();

        for(Room room : listRoomInfo) {
            roomResponseInfo.add(roomMapper.toRoomResponseInfo(room));
        }

        return GetRoomResponse.builder()
                .code(200)
                .message("Success")
                .authenticated(true)
                .listRoom(roomResponseInfo)
                .build();
    }

    @Override
    public UpdateRoomResponse updateRoom(UpdateRoomRequest updateRoomRequest) {
        long roomId = updateRoomRequest.getId();
        String newName = updateRoomRequest.getName();

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Room room = roomRepository.findByIdAndEmail(roomId, email)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        room.setName(newName);
        roomRepository.save(room);

        return UpdateRoomResponse.builder()
                .code(200)
                .message("Success")
                .authenticated(true)
                .roomId(roomId)
                .name(room.getName())
                .build();
    }
}
