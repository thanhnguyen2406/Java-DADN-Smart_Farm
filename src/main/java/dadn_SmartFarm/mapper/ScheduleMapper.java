package dadn_SmartFarm.mapper;

import dadn_SmartFarm.dto.ScheduleDTO.ScheduleRequestDTO.CreateScheduleRequest;
import dadn_SmartFarm.exception.AppException;
import dadn_SmartFarm.exception.ErrorCode;
import dadn_SmartFarm.model.Device;
import dadn_SmartFarm.model.Schedule;
import dadn_SmartFarm.repository.DeviceRepository;
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
