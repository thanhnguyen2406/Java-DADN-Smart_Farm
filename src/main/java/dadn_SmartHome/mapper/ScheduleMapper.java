package dadn_SmartHome.mapper;

import dadn_SmartHome.dto.ScheduleDTO.ScheduleRequestDTO.CreateScheduleRequest;
import dadn_SmartHome.exception.AppException;
import dadn_SmartHome.exception.ErrorCode;
import dadn_SmartHome.model.Device;
import dadn_SmartHome.model.Schedule;
import dadn_SmartHome.repository.DeviceRepository;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Builder
@Component
public class ScheduleMapper {
    private final DeviceRepository deviceRepository;

    public Schedule toSchedule(CreateScheduleRequest createScheduleRequest) {
        Device device = deviceRepository.findById(createScheduleRequest.getId_device())
                .orElseThrow(() -> new AppException(ErrorCode.DEVICE_NOT_FOUND));

        return Schedule.builder()
                .device(device)
                .status(createScheduleRequest.getStatus())
                .startDate(createScheduleRequest.getStartDate())
                .endDate(createScheduleRequest.getEndDate())
                .time_from(createScheduleRequest.getTime_from())
                .time_to(createScheduleRequest.getTime_to())
                .build();
    }
}
