package dadn_SmartHome.service.implement;

import dadn_SmartHome.dto.ScheduleDTO.ScheduleRequestDTO.CreateScheduleRequest;
import dadn_SmartHome.dto.ScheduleDTO.ScheduleResponse.CreateScheduleResponse;
import dadn_SmartHome.dto.ScheduleDTO.ScheduleResponse.GetScheduleResponse;
import dadn_SmartHome.exception.AppException;
import dadn_SmartHome.exception.ErrorCode;
import dadn_SmartHome.mapper.ScheduleMapper;
import dadn_SmartHome.model.Device;
import dadn_SmartHome.model.Schedule;
import dadn_SmartHome.model.enums.ScheduleType;
import dadn_SmartHome.repository.DeviceRepository;
import dadn_SmartHome.repository.RoomRepository;
import dadn_SmartHome.repository.ScheduleRepository;
import dadn_SmartHome.service.interf.IScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService implements IScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final RoomRepository roomRepository;
    private final DeviceRepository deviceRepository;


    @Override
    public GetScheduleResponse GetListWork(int month, int year, long id_room) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        if (!roomRepository.existsByRoomIdAndEmail(id_room, email)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Lấy danh sách deviceId từ room
        List<Long> listDeviceId = deviceRepository.findIdByRoomId(id_room)
                .orElseThrow(() -> new AppException(ErrorCode.DEVICE_NOT_FOUND));
        log.info("listDeviceId: {}", listDeviceId);

        // Khởi tạo danh sách Schedule trả về
        List<Schedule> allSchedules = new ArrayList<>();

        // Lặp qua từng deviceId và lấy Schedule theo tháng và năm
        for (long deviceId : listDeviceId) {
            // Lấy các schedule có startDate cụ thể và là loại ONCE
            List<Schedule> oneTimeSchedules = scheduleRepository.findSchedulesWithStartDateInMonth(deviceId, month, year)
                    .stream()
                    .filter(schedule -> schedule.getScheduleType() == ScheduleType.ONCE)
                    .toList();

            // Lấy các schedule lặp lại (DAILY / WEEKLY)
            List<Schedule> recurringSchedules = scheduleRepository.findRecurringSchedules(deviceId);

            // Lọc các schedule lặp lại rơi vào tháng và năm yêu cầu
            List<Schedule> filteredRecurring = recurringSchedules.stream()
                    .filter(schedule -> isScheduleInMonth(schedule, month, year))
                    .toList();

            // Gộp tất cả vào danh sách trả về
            allSchedules.addAll(oneTimeSchedules);
            allSchedules.addAll(filteredRecurring);
        }

        return GetScheduleResponse.builder()
                .code(200)
                .message("Success")
                .schedules(allSchedules)
                .build();
    }

    private boolean isScheduleInMonth(Schedule schedule, int month, int year) {
        switch (schedule.getScheduleType()) {
            case DAILY:
                return true;
            case WEEKLY:
                if (schedule.getWeekDay() == null) return false;

                // Lặp qua tất cả ngày trong tháng
                for (int day = 1; day <= YearMonth.of(year, month).lengthOfMonth(); day++) {
                    LocalDate date = LocalDate.of(year, month, day);

                    // So sánh với weekDay enum của bạn
                    if (date.getDayOfWeek().name().equals(schedule.getWeekDay().name())) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    @Override
    public CreateScheduleResponse CreateSchedule(CreateScheduleRequest createScheduleRequest) {
        Device device = deviceRepository.findById(createScheduleRequest.getId_device())
                .orElseThrow(() -> new AppException(ErrorCode.DEVICE_NOT_FOUND));

        // Tạo đối tượng Schedule từ CreateScheduleRequest
        Schedule schedule = Schedule.builder()
                .device(device)
                .status(createScheduleRequest.getStatus())
                .description(createScheduleRequest.getDescription())
                .scheduleType(createScheduleRequest.getScheduleType())
                .weekDay(createScheduleRequest.getWeekDay())
                .startDate(createScheduleRequest.getStartDate())
                .endDate(createScheduleRequest.getEndDate())
                .time_from(createScheduleRequest.getTime_from())
                .time_to(createScheduleRequest.getTime_to())
                .build();

        schedule = scheduleRepository.save(schedule);

        return CreateScheduleResponse.builder()
                .code(200)
                .message("Success")
                .authenticated(true)
                .id_device(schedule.getDevice().getId())
                .status(schedule.getStatus())
                .description(schedule.getDescription())
                .scheduleType(schedule.getScheduleType())
                .weekDay(schedule.getWeekDay())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .time_from(schedule.getTime_from())
                .time_to(schedule.getTime_to())
                .build();
    }
}
