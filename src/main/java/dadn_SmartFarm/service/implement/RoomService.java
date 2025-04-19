package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.dto.RoomDTO.RoomDtoModel.RoomResponseInfo;
import dadn_SmartFarm.dto.RoomDTO.RoomRequest.CreateRoomRequest;
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
import dadn_SmartFarm.utils.RoomKeyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private RoomKeyUtil roomKeyUtil;

    @Override
    public CreateRoomResponse createRoom(CreateRoomRequest request) {
        Room room = new Room();
        room.setName(request.getName());
        room.setRoomKey(request.getRoomKey());
        roomRepository.save(room);

        return CreateRoomResponse.builder()
                .code(200)
                .message("Success")
                .id(room.getId())
                .name(request.getName())
                .build();
    }

    @Override
    public GetRoomResponse getRoomResponse() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        List<Room> listRoomInfo = roomRepository.getRoomsByUsername(email);

        List<RoomResponseInfo> roomResponseInfo = new ArrayList<>();

        for(Room room : listRoomInfo) {
            roomResponseInfo.add(roomMapper.toRoomResponseInfo(room));
        }

        return GetRoomResponse.builder()
                .code(200)
                .message("Success")
                .listRoom(roomResponseInfo)
                .build();
    }

    @Override
    public UpdateRoomResponse updateRoom(UpdateRoomRequest updateRoomRequest) {
        long roomId = updateRoomRequest.getId();

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Room room = roomRepository.findByIdAndUsername(roomId, email)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        room.setName(updateRoomRequest.getName());
        room.setRoomKey(updateRoomRequest.getRoomKey());
        roomRepository.save(room);

        return UpdateRoomResponse.builder()
                .code(200)
                .message("Success")
                .roomId(roomId)
                .name(room.getName())
                .build();
    }

    @Override
    public Response encodeRoom(long id) {
        try {
            Room existingRoom = roomRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
            String roomKey = RoomKeyUtil.encodeRoomKey(existingRoom.getId(), existingRoom.getName());
            return Response.builder()
                    .code(200)
                    .message("Room encoded successfully")
                    .encodedRoom(roomKey)
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while encoding room: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response assignRoom(String encodedRoom) {
        try {
            RoomKeyUtil.DecodedRoomKey decoded = RoomKeyUtil.decodeRoomKey(encodedRoom);

            Room room = roomRepository.findById(decoded.roomId)
                    .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!room.getListUsername().contains(username)) {
                room.getListUsername().add(username);
                roomRepository.save(room);
            }
            roomRepository.save(room);
            return Response.builder()
                    .code(200)
                    .message("Room assigned successfully")
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while encoding room: " + e.getMessage())
                    .build();
        }
    }
}
