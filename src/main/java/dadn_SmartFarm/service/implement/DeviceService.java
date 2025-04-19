package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.dto.DataInfo.DataDTO;
import dadn_SmartFarm.dto.DeviceDTO.DeviceDTO;
import dadn_SmartFarm.dto.DeviceDTO.DeviceRoomDTO;
import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.exception.AppException;
import dadn_SmartFarm.exception.ErrorCode;
import dadn_SmartFarm.mapper.DeviceMapper;
import dadn_SmartFarm.model.Device;
import dadn_SmartFarm.model.FeedInfo;
import dadn_SmartFarm.model.enums.Status;
import dadn_SmartFarm.model.enums.DeviceType;
import dadn_SmartFarm.repository.DeviceRepository;
import dadn_SmartFarm.repository.RoomRepository;
import dadn_SmartFarm.service.interf.IDataInfoService;
import dadn_SmartFarm.service.interf.IDeviceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DeviceService implements IDeviceService {
    DeviceRepository deviceRepository;
    DeviceMapper deviceMapper;
    RoomRepository roomRepository;
    MqttService mqttService;
    IDataInfoService dataInfoService;

    @Override
    public Response addDevice(DeviceDTO request) {
        Device newDevice = deviceMapper.toDevice(request);
        if (checkFeedsList("add", newDevice)) {
            throw new AppException(ErrorCode.FEED_EXISTED);
        }
        newDevice.setStatus(Status.INACTIVE);
        deviceRepository.save(newDevice);

        return Response.builder()
                .code(200)
                .message("Device added successfully")
                .build();
    }

    @Override
    public Response updateDevice(DeviceDTO request) {
        Device existingDevice = deviceRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.DEVICE_NOT_FOUND));
        existingDevice.setName(request.getName());
        existingDevice.setStatus(request.getStatus());
        existingDevice.setFeedsList(request.getFeedsList());
        if (checkFeedsList("update", existingDevice)) {
            throw new AppException(ErrorCode.FEED_EXISTED);
        }
        deviceRepository.save(existingDevice);

        //Connect with MQTT
        if (existingDevice.getType() == DeviceType.SENSOR) {
            if (existingDevice.getStatus() == Status.ACTIVE) {
                mqttService.connectDevice(existingDevice.getId(), existingDevice.getFeedsList());
            } else if (existingDevice.getStatus() == Status.INACTIVE) {
                mqttService.disconnectDevice(existingDevice.getId());
            }
        } else {
            existingDevice.getFeedsList().forEach((s, feedInfo) -> {
                DataDTO dataDTO = new DataDTO();

                dataDTO.setFeed_key(s);
                dataDTO.setFeed_id(feedInfo.getFeedId());
                if (existingDevice.getStatus() == Status.ACTIVE) {
                    dataDTO.setValue("1");
                } else if (existingDevice.getStatus() == Status.INACTIVE) {
                    dataDTO.setValue("0");
                }
                dataInfoService.sendData(dataDTO);
            });
        }
        return Response.builder()
                .code(200)
                .message("Device updated successfully")
                .build();
    }

    @Override
    public Response deleteDevice(long id) {
        if (!deviceRepository.existsById(id)) {
            throw new AppException(ErrorCode.DEVICE_NOT_FOUND);
        }
        deviceRepository.deleteById(id);
        return Response.builder()
                .code(200)
                .message("Device deleted successfully")
                .build();
    }

    @Override
    public Response assignDeviceToRoom(DeviceRoomDTO request) {
        try {
            if (!roomRepository.existsById(request.getRoomId())) {
                throw new AppException(ErrorCode.ROOM_NOT_FOUND);
            }
            Device existingDevice = deviceRepository.findById(request.getDeviceId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEVICE_NOT_FOUND));
            existingDevice.setRoomId(request.getRoomId());
            deviceRepository.save(existingDevice);
            return Response.builder()
                    .code(200)
                    .message("Device assigned to room successfully")
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while assigning device to room: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response dismissDeviceToRoom(long id) {
        try {
            Device existingDevice = deviceRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.DEVICE_NOT_FOUND));
            existingDevice.setRoomId(null);
            deviceRepository.save(existingDevice);
            return Response.builder()
                    .code(200)
                    .message("Device dismissed from room successfully")
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while dismissing device from room: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getDevicesByRoomId(long id, Pageable pageable) {
        try {
            if (!roomRepository.existsById(id)) {
                throw new AppException(ErrorCode.ROOM_NOT_FOUND);
            }
            Page<Device> devicePage = deviceRepository.findByRoomId(id, pageable);
            if (devicePage.isEmpty()) {
                return Response.builder()
                        .code(200)
                        .message("No devices found")
                        .build();
            }

            List<DeviceDTO> deviceList = devicePage.getContent().stream().map(deviceMapper::toDeviceDTO).toList();
            return Response.builder()
                    .code(200)
                    .message("All devices of rooms fetched successfully")
                    .listDeviceDTO(deviceList)
                    .currentPage(devicePage.getNumber())
                    .totalElements((int) devicePage.getTotalElements())
                    .totalPages(devicePage.getTotalPages())
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while fetching device from room: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getUnassignedDevices(Pageable pageable) {
        try {
            Page<Device> devicePage = deviceRepository.findByRoomId(null, pageable);
            if (devicePage.isEmpty()) {
                return Response.builder()
                        .code(200)
                        .message("No devices found")
                        .build();
            }

            List<DeviceDTO> deviceList = devicePage.getContent().stream().map(deviceMapper::toDeviceDTO).toList();
            return Response.builder()
                    .code(200)
                    .message("All unassigned devices of rooms fetched successfully")
                    .listDeviceDTO(deviceList)
                    .currentPage(devicePage.getNumber())
                    .totalElements((int) devicePage.getTotalElements())
                    .totalPages(devicePage.getTotalPages())
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while fetching unassigned device from room: " + e.getMessage())
                    .build();
        }
    }


    public boolean checkFeedsList(String typeService, Device device) {
        Map<String, FeedInfo> currentFeeds = device.getFeedsList();

        if (currentFeeds == null || currentFeeds.isEmpty()) {
            return false;
        }

        if ("add".equalsIgnoreCase(typeService)) {
            List<Map<String, Long>> feedsList = deviceRepository.findAllFeeds();

            return feedsList.stream().anyMatch(existingFeeds ->
                    existingFeeds.entrySet().stream()
                            .anyMatch(entry ->
                                    currentFeeds.containsKey(entry.getKey()) &&
                                            currentFeeds.get(entry.getKey()).equals(entry.getValue())
                            )
            );
        }

        if ("update".equalsIgnoreCase(typeService)) {
            List<Device> otherDevices = deviceRepository.findByIdNot(device.getId());

            return otherDevices.stream().anyMatch(otherDevice -> {
                Map<String, FeedInfo> otherFeeds = otherDevice.getFeedsList();
                return otherFeeds != null && otherFeeds.entrySet().stream()
                        .anyMatch(entry ->
                                currentFeeds.containsKey(entry.getKey()) &&
                                        currentFeeds.get(entry.getKey()).equals(entry.getValue())
                        );
            });
        }
        return false;
    }

    public boolean checkFeedIdExists(Long deviceId, Long feedId) {
        return deviceRepository.findById(deviceId)
                .map(device -> device.getFeedsList().values().stream()
                        .anyMatch(feed -> feedId.equals(feed.getFeedId())))
                .orElse(false);
    }

    public double getThresholdMaxByFeedId(Long feedId) {
        return deviceRepository.findDeviceByFeedIdInJson(feedId)
                .flatMap(device -> device.getFeedsList().values().stream()
                        .filter(info -> Objects.equals(info.getFeedId(), feedId))
                        .findFirst())
                .map(FeedInfo::getThreshold_max)
                .orElse(Double.NaN);
    }

    public double getThresholdMinByFeedId(Long feedId) {
        return deviceRepository.findDeviceByFeedIdInJson(feedId)
                .flatMap(device -> device.getFeedsList().values().stream()
                        .filter(info -> Objects.equals(info.getFeedId(), feedId))
                        .findFirst())
                .map(FeedInfo::getThreshold_min)
                .orElse(Double.NaN);
    }
}
