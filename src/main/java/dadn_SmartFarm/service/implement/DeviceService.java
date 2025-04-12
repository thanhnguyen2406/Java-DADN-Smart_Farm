package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.dto.DeviceDTO.DeviceDTO;
import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.exception.AppException;
import dadn_SmartFarm.exception.ErrorCode;
import dadn_SmartFarm.mapper.DeviceMapper;
import dadn_SmartFarm.model.Device;
import dadn_SmartFarm.model.FeedInfo;
import dadn_SmartFarm.model.enums.Status;
import dadn_SmartFarm.model.enums.DeviceType;
import dadn_SmartFarm.repository.DeviceRepository;
import dadn_SmartFarm.repository.DeviceTriggerRepository;
import dadn_SmartFarm.service.implement.MqttService;
import dadn_SmartFarm.service.interf.IDeviceService;
import dadn_SmartFarm.utils.FeedEncoder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DeviceService implements IDeviceService {
    DeviceRepository deviceRepository;
    DeviceTriggerRepository deviceTriggerRepository;
    DeviceMapper deviceMapper;
    MqttService mqttService;

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
        if (existingDevice.getType() == DeviceType.SENSOR_DATA || existingDevice.getType() == DeviceType.SENSOR_TRIGGER) {
            if (existingDevice.getStatus() == Status.ACTIVE) {
                if (existingDevice.getType() == DeviceType.SENSOR_TRIGGER) {
                    List<String> feedKeys = new ArrayList<>(existingDevice.getFeedsList().keySet());
                    if (!deviceTriggerRepository.existsBySensorFeedKeyIn(feedKeys)) {
                        throw new AppException(ErrorCode.TRIGGER_DEVICE_NOT_FOUND);
                    }
                }
                mqttService.connectDevice(existingDevice.getId(), existingDevice.getType(), existingDevice.getFeedsList());
            } else if (existingDevice.getStatus() == Status.INACTIVE) {
                mqttService.disconnectDevice(existingDevice.getId());
            }
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

//    @Override
//    public Response encodeDevice(long id) {
//        Device existingDevice = deviceRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCode.DEVICE_NOT_FOUND));
//        String encodedFeeds = FeedEncoder.encodeFeeds(existingDevice.getFeedsList());
//        return Response.builder()
//                .code(200)
//                .message("Device encoded successfully")
//                .encodedFeeds(encodedFeeds)
//                .build();
//    }
//
//    @Override
//    public Response assignDevice(String encodedFeeds) {
//        if (!FeedEncoder.isValidEncodedString(encodedFeeds)) {
//            throw new AppException(ErrorCode.ENCODED_DEVICE_INVALID);
//        }
//        Map<String, FeedInfo> feeds = FeedEncoder.decodeFeeds(encodedFeeds);
//        Device existingDevice = deviceRepository.findByFeedsList(feeds);
//
//        var context = SecurityContextHolder.getContext();
//        String email = context.getAuthentication().getName();
//
//        existingDevice.setUserEmail(email);
//        deviceRepository.save(existingDevice);
//        return Response.builder()
//                .code(200)
//                .message("Device has assigned successfully")
//                .build();
//    }

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
}
