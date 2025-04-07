package dadn_SmartHome.service.implement;

import dadn_SmartHome.dto.ScheduleDTO.ScheduleRequestDTO.CreateScheduleRequest;
import dadn_SmartHome.dto.ScheduleDTO.ScheduleResponse.CreateScheduleResponse;
import dadn_SmartHome.dto.ScheduleDTO.ScheduleResponse.DeleteScheduleResponse;
import dadn_SmartHome.dto.ScheduleDTO.ScheduleResponse.GetScheduleResponse;
import dadn_SmartHome.exception.AppException;
import dadn_SmartHome.exception.ErrorCode;
import dadn_SmartHome.mapper.ScheduleMapper;
import dadn_SmartHome.model.Device;
import dadn_SmartHome.model.Schedule;
import dadn_SmartHome.model.enums.ScheduleType;
import dadn_SmartHome.model.enums.WeekDay;
import dadn_SmartHome.repository.DeviceRepository;
import dadn_SmartHome.repository.RoomRepository;
import dadn_SmartHome.repository.ScheduleRepository;
import dadn_SmartHome.service.interf.IScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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

        // Lấy tất cả các lịch hiện tại của thiết bị
        List<Schedule> existingSchedules = scheduleRepository.findByDeviceId(device.getId());

        // Kiểm tra nếu có lịch nào trùng lắp
        boolean isConflict = existingSchedules.stream()
                .anyMatch(existingSchedule -> isOverlapping(existingSchedule, createScheduleRequest));

        if (isConflict) {
            throw new AppException(ErrorCode.SCHEDULE_TIME_OVERLAP);
        }

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

        // Lưu lịch vào cơ sở dữ liệu
        schedule = scheduleRepository.save(schedule);

        // Trả về kết quả
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

    @Override
    public DeleteScheduleResponse DeleteSchedule(long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));

        scheduleRepository.delete(schedule);
        return DeleteScheduleResponse.builder()
                .code(200)
                .message("Success")
                .authenticated(true)
                .build();
    }

    private boolean isTimeOverlapping(LocalTime timeFrom1, LocalTime timeTo1, LocalTime timeFrom2, LocalTime timeTo2) {
        return !(timeTo1.isBefore(timeFrom2) || timeFrom1.isAfter(timeTo2));
    }

    private boolean isOverlapping(Schedule existingSchedule, CreateScheduleRequest scheduleRequest) {
        // Kiểm tra lịch có cùng thiết bị hay không
        if (existingSchedule.getDevice().getId() != scheduleRequest.getId_device()) {
            return false; // Không cùng thiết bị thì không chồng lắp
        }

        // Kiểm tra các loại lịch
        switch (scheduleRequest.getScheduleType()) {
            case ONCE:
                return isOnceScheduleOverlapping(existingSchedule, scheduleRequest) ||
                        isDailyOrWeeklyOverlappingWithOnce(existingSchedule, scheduleRequest);
            case DAILY:
                return isDailyScheduleOverlapping(existingSchedule, scheduleRequest) ||
                        isOnceOrWeeklyOverlappingWithDaily(existingSchedule, scheduleRequest);
            case WEEKLY:
                return isWeeklyScheduleOverlapping(existingSchedule, scheduleRequest) ||
                        isOnceOrDailyOverlappingWithWeekly(existingSchedule, scheduleRequest);
            default:
                return false;
        }
    }

    private boolean isDailyOrWeeklyOverlappingWithOnce(Schedule existingSchedule, CreateScheduleRequest scheduleRequest) {
        if (existingSchedule.getScheduleType() == ScheduleType.DAILY || existingSchedule.getScheduleType() == ScheduleType.WEEKLY) {
            // Kiểm tra nếu lịch hiện tại có cả startDate và endDate khác null
            if (existingSchedule.getStartDate() != null && existingSchedule.getEndDate() != null &&
                    !scheduleRequest.getStartDate().isAfter(existingSchedule.getEndDate()) &&
                    !scheduleRequest.getEndDate().isBefore(existingSchedule.getStartDate())) {
                return isTimeOverlapping(scheduleRequest.getTime_from(), scheduleRequest.getTime_to(),
                        existingSchedule.getTime_from(), existingSchedule.getTime_to());
            }
        }
        return false;
    }

    private boolean isOnceOrWeeklyOverlappingWithDaily(Schedule existingSchedule, CreateScheduleRequest scheduleRequest) {
        // Nếu lịch hiện tại là ONCE hoặc WEEKLY
        if (existingSchedule.getScheduleType() == ScheduleType.ONCE || existingSchedule.getScheduleType() == ScheduleType.WEEKLY) {
            // Kiểm tra nếu ngày của lịch DAILY nằm trong khoảng thời gian của lịch ONCE/WEEKLY
            if (scheduleRequest.getWeekDay() != null && existingSchedule.getWeekDay().equals(scheduleRequest.getWeekDay())) {
                // Kiểm tra trùng lắp về thời gian trong ngày
                return isTimeOverlapping(scheduleRequest.getTime_from(), scheduleRequest.getTime_to(),
                        existingSchedule.getTime_from(), existingSchedule.getTime_to());
            }
        }
        return false;
    }

    private boolean isOnceOrDailyOverlappingWithWeekly(Schedule existingSchedule, CreateScheduleRequest scheduleRequest) {
        // Kiểm tra nếu lịch hiện tại là WEEKLY
        if (existingSchedule.getScheduleType() == ScheduleType.WEEKLY) {
            // Kiểm tra nếu cả hai weekDay đều không null và bằng nhau
            if (existingSchedule.getWeekDay() != null && scheduleRequest.getWeekDay() != null &&
                    existingSchedule.getWeekDay().equals(scheduleRequest.getWeekDay())) {
                return isTimeOverlapping(
                        scheduleRequest.getTime_from(), scheduleRequest.getTime_to(),
                        existingSchedule.getTime_from(), existingSchedule.getTime_to()
                );
            }
        }
        return false;
    }

    private boolean isOnceScheduleOverlapping(Schedule existingSchedule, CreateScheduleRequest scheduleRequest) {
        // Kiểm tra nếu lịch hiện tại có startDate khác null
        if (existingSchedule.getStartDate() != null && existingSchedule.getStartDate().equals(scheduleRequest.getStartDate())) {
            return isTimeOverlapping(scheduleRequest.getTime_from(), scheduleRequest.getTime_to(),
                    existingSchedule.getTime_from(), existingSchedule.getTime_to());
        }

        // Kiểm tra nếu lịch mới kéo dài qua nhiều ngày và lịch hiện tại có cả startDate và endDate khác null
        if (existingSchedule.getStartDate() != null && existingSchedule.getEndDate() != null &&
                !scheduleRequest.getStartDate().isAfter(existingSchedule.getEndDate()) &&
                !scheduleRequest.getEndDate().isBefore(existingSchedule.getStartDate())) {
            return isTimeOverlapping(scheduleRequest.getTime_from(), scheduleRequest.getTime_to(),
                    existingSchedule.getTime_from(), existingSchedule.getTime_to());
        }

        return false;
    }

    private boolean isDailyScheduleOverlapping(Schedule existingSchedule, CreateScheduleRequest scheduleRequest) {
        // Kiểm tra nếu lịch hiện tại là DAILY
        if (existingSchedule.getScheduleType() == ScheduleType.DAILY) {
            // Nếu không có weekDay, giả định lịch áp dụng cho tất cả các ngày
            if (scheduleRequest.getWeekDay() == null || existingSchedule.getWeekDay() == null) {
                return isTimeOverlapping(
                        scheduleRequest.getTime_from(), scheduleRequest.getTime_to(),
                        existingSchedule.getTime_from(), existingSchedule.getTime_to()
                );
            }

            // Nếu có weekDay, kiểm tra xem có cùng ngày trong tuần hay không
            if (scheduleRequest.getWeekDay().equals(existingSchedule.getWeekDay())) {
                return isTimeOverlapping(
                        scheduleRequest.getTime_from(), scheduleRequest.getTime_to(),
                        existingSchedule.getTime_from(), existingSchedule.getTime_to()
                );
            }
        }

        return false;
    }

    private boolean isWeeklyScheduleOverlapping(Schedule existingSchedule, CreateScheduleRequest scheduleRequest) {
        // Kiểm tra nếu lịch WEEKLY trùng ngày với lịch DAILY hoặc WEEKLY
        if (existingSchedule.getWeekDay() != null && existingSchedule.getWeekDay().equals(scheduleRequest.getWeekDay())) {
            return isTimeOverlapping(scheduleRequest.getTime_from(), scheduleRequest.getTime_to(),
                    existingSchedule.getTime_from(), existingSchedule.getTime_to());
        }
        return false;
    }
}
