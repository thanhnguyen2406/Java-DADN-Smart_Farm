package dadn_SmartFarm.mapper;

import dadn_SmartFarm.dto.DeviceTriggerDTO.DeviceTriggerDTO;
import dadn_SmartFarm.model.DeviceTrigger;
import org.springframework.stereotype.Component;

@Component
public class DeviceTriggerMapper {
    public DeviceTrigger toDeviceTrigger (DeviceTriggerDTO deviceTriggerDTO) {
        return DeviceTrigger.builder()
                .id(deviceTriggerDTO.getId())
                .status(deviceTriggerDTO.getStatus())
                .valueSend(deviceTriggerDTO.getValueSend())
                .thresholdCondition(deviceTriggerDTO.getCondition())
                .sensorFeedKey(deviceTriggerDTO.getSensorFeedKey())
                .controlFeedKey(deviceTriggerDTO.getControlFeedKey())
                .build();
    }

    public DeviceTriggerDTO toDeviceTriggerDTO (DeviceTrigger deviceTrigger) {
        return DeviceTriggerDTO.builder()
                .id(deviceTrigger.getId())
                .status(deviceTrigger.getStatus())
                .valueSend(deviceTrigger.getValueSend())
                .condition(deviceTrigger.getThresholdCondition())
                .sensorFeedKey(deviceTrigger.getSensorFeedKey())
                .controlFeedKey(deviceTrigger.getControlFeedKey())
                .build();
    }
}
