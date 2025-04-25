package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.dto.DeviceTriggerDTO.DeviceTriggerDTO;
import dadn_SmartFarm.dto.DeviceTriggerDTO.FeedDTO;
import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.exception.AppException;
import dadn_SmartFarm.exception.ErrorCode;
import dadn_SmartFarm.mapper.DeviceTriggerMapper;
import dadn_SmartFarm.model.Device;
import dadn_SmartFarm.model.DeviceTrigger;
import dadn_SmartFarm.model.enums.DeviceType;
import dadn_SmartFarm.model.enums.Status;
import dadn_SmartFarm.repository.DeviceRepository;
import dadn_SmartFarm.repository.DeviceTriggerRepository;
import dadn_SmartFarm.repository.RoomRepository;
import dadn_SmartFarm.service.interf.IDeviceTriggerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DeviceTriggerService implements IDeviceTriggerService {
    DeviceTriggerRepository deviceTriggerRepository;
    DeviceTriggerMapper deviceTriggerMapper;
    DeviceRepository deviceRepository;
    RoomRepository roomRepository;

    @Override
    public Response addTrigger(DeviceTriggerDTO deviceTriggerDTO) {
        try{
            String sensorKey = deviceTriggerDTO.getSensorFeedKey();
            String controlKey = deviceTriggerDTO.getControlFeedKey();

            if (deviceTriggerRepository.existsBySensorFeedKeyAndControlFeedKeyAndThresholdCondition(sensorKey, controlKey, deviceTriggerDTO.getCondition()))
                throw new AppException(ErrorCode.TRIGGER_EXISTED);

            checkTrigger(sensorKey, controlKey);

            DeviceTrigger deviceTrigger = deviceTriggerMapper.toDeviceTrigger(deviceTriggerDTO);
            deviceTrigger.setStatus(Status.INACTIVE);
            deviceTriggerRepository.save(deviceTrigger);

            return Response.builder()
                    .code(200)
                    .message("New trigger added successfully")
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while adding trigger: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response deleteTrigger(long Id) {
        try{
            if (deviceTriggerRepository.existsById(Id
            ))
                throw new AppException(ErrorCode.TRIGGER_NOT_FOUND);

            deviceTriggerRepository.deleteById(Id);

            return Response.builder()
                    .code(200)
                    .message("Trigger deleted successfully")
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while deleting trigger: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response updateTrigger(DeviceTriggerDTO deviceTriggerDTO) {
        try{
            if (!deviceTriggerRepository.existsById(deviceTriggerDTO.getId()))
                throw new AppException(ErrorCode.TRIGGER_NOT_FOUND);

            DeviceTrigger deviceTrigger = deviceTriggerMapper.toDeviceTrigger(deviceTriggerDTO);

            checkTrigger(deviceTrigger.getSensorFeedKey(), deviceTrigger.getControlFeedKey());
            deviceTriggerRepository.save(deviceTrigger);

            return Response.builder()
                    .code(200)
                    .message("Trigger updated successfully")
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while updating trigger: " + e.getMessage())
                    .build();
        }
    }



    @Override
    public Response getTriggersByDeviceId(Long deviceId, Pageable pageable) {
        try{
            Device device = deviceRepository.findById(deviceId)
                    .orElseThrow(() -> new AppException(ErrorCode.DEVICE_NOT_FOUND));
            List<String> feedKeys = new ArrayList<>(device.getFeedsList().keySet());
            Page<DeviceTrigger> triggers = deviceTriggerRepository.findBySensorFeedKeyIn(feedKeys, pageable);
            if(feedKeys.isEmpty() || triggers.getContent().isEmpty()) {
                return Response.builder()
                        .code(200)
                        .message("No triggers found for this device")
                        .build();
            }
            List<DeviceTriggerDTO> dtos = triggers.getContent().stream()
                    .map(deviceTriggerMapper::toDeviceTriggerDTO).toList();
            return Response.builder()
                    .code(200)
                    .message("Trigger of the device fetched successfully")
                    .listDeviceTriggersDTO(dtos)
                    .currentPage(triggers.getNumber())
                    .totalElements((int) triggers.getTotalElements())
                    .totalPages(triggers.getTotalPages())
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while updating trigger: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getSensorFeedsByRoom(long roomId) {
        try {
            if (!roomRepository.existsById(roomId)) {
                throw new AppException(ErrorCode.ROOM_NOT_FOUND);
            }
            List<Device> deviceSensorList = deviceRepository.findByTypeAndRoomId(DeviceType.SENSOR, roomId);
            List<FeedDTO> feedDTOList = new ArrayList<>();
            for (Device deviceSensor : deviceSensorList) {
                for (String feedKey : deviceSensor.getFeedsList().keySet()) {
                    feedDTOList.add(FeedDTO.builder()
                                    .feedKey(feedKey)
                                    .deviceName(deviceSensor.getName())
                                    .build());
                }
            }

            if(feedDTOList.isEmpty()) {
                return Response.builder()
                        .code(200)
                        .message("No sensor feeds of this room is found")
                        .build();
            }

            return Response.builder()
                    .code(200)
                    .message("Sensor feeds of this room fetched successfully")
                    .listFeedDTO(feedDTOList)
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while get sensor feed: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getControlFeedsByRoom(long roomId) {
        try {
            if (!roomRepository.existsById(roomId)) {
                throw new AppException(ErrorCode.ROOM_NOT_FOUND);
            }
            List<Device> deviceSensorList = deviceRepository.findByTypeAndRoomId(DeviceType.CONTROL, roomId);
            List<FeedDTO> feedDTOList = new ArrayList<>();
            for (Device deviceSensor : deviceSensorList) {
                for (String feedKey : deviceSensor.getFeedsList().keySet()) {
                    feedDTOList.add(FeedDTO.builder()
                            .feedKey(feedKey)
                            .deviceName(deviceSensor.getName())
                            .build());
                }
            }

            if(feedDTOList.isEmpty()) {
                return Response.builder()
                        .code(200)
                        .message("No control feeds of this room is found")
                        .build();
            }

            return Response.builder()
                    .code(200)
                    .message("Control feeds of this room fetched successfully")
                    .listFeedDTO(feedDTOList)
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while get control feed: " + e.getMessage())
                    .build();
        }
    }

    public void checkTrigger(String sensorKey, String controlKey) {
        if (deviceRepository.existsFeedKey(sensorKey) <= 0) {
            throw new AppException(ErrorCode.FEED_SENSOR_NOT_FOUND);
        }
        if (deviceRepository.existsFeedKey(controlKey) <= 0) {
            throw new AppException(ErrorCode.FEED_CONTROL_NOT_FOUND);
        }
        if (deviceRepository.existsSensorTriggerFeedKey(sensorKey) <= 0) {
            throw new AppException(ErrorCode.FEED_SENSOR_NOT_MATCH);
        }
        if (deviceRepository.existsControlFeedKey(controlKey) <= 0) {
            throw new AppException(ErrorCode.FEED_CONTROL_NOT_MATCH);
        }
    }
}
