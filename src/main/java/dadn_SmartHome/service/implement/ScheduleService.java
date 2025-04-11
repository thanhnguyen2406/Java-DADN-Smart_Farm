package dadn_SmartHome.service.implement;

import dadn_SmartHome.dto.ScheduleDTO.ScheduleRequestDTO.CreateScheduleRequest;
import dadn_SmartHome.dto.ScheduleDTO.ScheduleResponse.CreateScheduleResponse;
import dadn_SmartHome.dto.ScheduleDTO.ScheduleResponse.DeleteScheduleResponse;
import dadn_SmartHome.dto.ScheduleDTO.ScheduleResponse.GetScheduleResponse;
import dadn_SmartHome.exception.AppException;
import dadn_SmartHome.exception.ErrorCode;
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
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService implements IScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final RoomRepository roomRepository;
    private final DeviceRepository deviceRepository;


    @Override
    public GetScheduleResponse GetListWork(int month, int year, long id_room) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        if (!roomRepository.existsByRoomIdAndEmail(id_room, email)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // get list deviceId from room
        List<Long> listDeviceId = deviceRepository.findIdByRoomId(id_room)
                .orElseThrow(() -> new AppException(ErrorCode.DEVICE_NOT_FOUND));
        log.info("listDeviceId: {}", listDeviceId);

        // initialize list Schedule
        List<Schedule> allSchedules = new ArrayList<>();

        // Loop through each deviceId and get Schedule by month and year
        for (long deviceId : listDeviceId) {
            // Get schedules that have a specific startDate and are of type ONCE
            List<Schedule> oneTimeSchedules = scheduleRepository.findSchedulesWithStartDateInMonth(deviceId, month, year)
                    .stream()
                    .filter(schedule -> schedule.getScheduleType() == ScheduleType.ONCE)
                    .toList();

            // Get the recurring schedules (DAILY / WEEKLY)
            List<Schedule> recurringSchedules = scheduleRepository.findRecurringSchedules(deviceId);

            // Filter recurring schedules that fall into the required month and year
            List<Schedule> filteredRecurring = recurringSchedules.stream()
                    .filter(schedule -> isScheduleInMonth(schedule, month, year))
                    .toList();

            // Merge all into return list
            allSchedules.addAll(oneTimeSchedules);
            allSchedules.addAll(filteredRecurring);
        }

        return GetScheduleResponse.builder()
                .code(200)
                .message("Success")
                .schedules(allSchedules)
                .build();
    }

    @Override
    public CreateScheduleResponse CreateSchedule(CreateScheduleRequest createScheduleRequest) {
        Device device = deviceRepository.findById(createScheduleRequest.getId_device())
                .orElseThrow(() -> new AppException(ErrorCode.DEVICE_NOT_FOUND));

        // Get all current calendars of the device
        List<Schedule> existingSchedules = scheduleRepository.findByDeviceId(device.getId());

        // Check if any calendars overlap
        boolean isConflict = existingSchedules.stream()
                .anyMatch(existingSchedule -> isOverlapping(existingSchedule, createScheduleRequest));

        if (isConflict) {
            throw new AppException(ErrorCode.SCHEDULE_TIME_OVERLAP);
        }

        // Create new Object Schedule from CreateScheduleRequest
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

        // Save schedule into Database
        schedule = scheduleRepository.save(schedule);

        // Return result
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

    //--------------------------------------------SUPPORT FUNCTION-----------------------------------------------------------------------

    private boolean isTimeOverlapping(LocalTime timeFrom1, LocalTime timeTo1, LocalTime timeFrom2, LocalTime timeTo2) {
        if (timeFrom1 == null || timeTo1 == null || timeFrom2 == null || timeTo2 == null) {
            return false;
        }
        return !(timeTo1.isBefore(timeFrom2) || timeFrom1.isAfter(timeTo2));
    }

    private boolean isOverlapping(Schedule existingSchedule, CreateScheduleRequest scheduleRequest) {
        if (existingSchedule.getDevice().getId() != scheduleRequest.getId_device()) {
            return false;
        }

        return switch (scheduleRequest.getScheduleType()) {
            case ONCE -> {
                if (scheduleRequest.getStartDate() == null || scheduleRequest.getEndDate() == null) {
                    throw new AppException(ErrorCode.REQUEST_START_DATE_AND_END_DATE);
                } else if (scheduleRequest.getStartDate().isAfter(scheduleRequest.getEndDate())) {
                    throw new AppException(ErrorCode.START_DATE_AFTER_END_DATE);
                }
                yield isOnceScheduleOverlapping(existingSchedule, scheduleRequest);
            }
            case DAILY -> {
                if (scheduleRequest.getTime_from().isAfter(scheduleRequest.getTime_to())) {
                    throw new AppException(ErrorCode.REQUEST_START_TIME_BEFORE_END_TIME);
                }
                yield isDailyScheduleOverlapping(existingSchedule, scheduleRequest);
            }
            case WEEKLY -> {
                if (scheduleRequest.getWeekDay() == null) {
                    throw new AppException(ErrorCode.REQUEST_WEEKDAY);
                }
                yield isWeeklyScheduleOverlapping(existingSchedule, scheduleRequest);
            }
        };
    }

    private boolean isOnceScheduleOverlapping(Schedule existingSchedule, CreateScheduleRequest scheduleRequest) {
        LocalDate newStartDate = scheduleRequest.getStartDate();
        LocalDate newEndDate = scheduleRequest.getEndDate();

        if (existingSchedule.getScheduleType() == ScheduleType.ONCE) {
            LocalDate existingStartDate = existingSchedule.getStartDate();
            LocalDate existingEndDate = existingSchedule.getEndDate();

            if (existingStartDate == null || existingEndDate == null) return false;

            boolean isDateOverlapping = !newStartDate.isAfter(existingEndDate) && !newEndDate.isBefore(existingStartDate);

            if (isDateOverlapping) {
                return isTimeOverlapping(existingSchedule.getTime_from(), existingSchedule.getTime_to(),
                        scheduleRequest.getTime_from(), scheduleRequest.getTime_to());
            }
        }

        else if (existingSchedule.getScheduleType() == ScheduleType.DAILY) {
            return isTimeOverlapping(scheduleRequest.getTime_from(), scheduleRequest.getTime_to(),
                    existingSchedule.getTime_from(), existingSchedule.getTime_to());
        }

        else if (existingSchedule.getScheduleType() == ScheduleType.WEEKLY) {
            String existingWeekDay = existingSchedule.getWeekDay().toString();

            // Check if the range of [newStartDate; newEndDate] is more than a week
            if (newEndDate.isAfter(newStartDate.plusDays(6))) {
                // Directly check time overlap for the specified weekday
                return isTimeOverlapping(scheduleRequest.getTime_from(), scheduleRequest.getTime_to(),
                        existingSchedule.getTime_from(), existingSchedule.getTime_to());
            } else {
                // Iterate through the date range of the ONCE schedule
                for (LocalDate date = newStartDate; !date.isAfter(newEndDate); date = date.plusDays(1)) {
                    if(date.getDayOfWeek().toString().equals(existingWeekDay)) {
                        if (isTimeOverlapping(scheduleRequest.getTime_from(), scheduleRequest.getTime_to(),
                                existingSchedule.getTime_from(), existingSchedule.getTime_to())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean isDailyScheduleOverlapping(Schedule existingSchedule, CreateScheduleRequest scheduleRequest) {
        ScheduleType existingType = existingSchedule.getScheduleType();

        LocalTime newTimeFrom = scheduleRequest.getTime_from();
        LocalTime newTimeTo = scheduleRequest.getTime_to();

        if(existingType == ScheduleType.DAILY || existingType == ScheduleType.WEEKLY || existingType == ScheduleType.ONCE) {
            return isTimeOverlapping(newTimeFrom, newTimeTo, scheduleRequest.getTime_from(), scheduleRequest.getTime_to());
        }

        return false;
    }

    private boolean isWeeklyScheduleOverlapping(Schedule existingSchedule, CreateScheduleRequest scheduleRequest) {

        LocalTime newTimeFrom = scheduleRequest.getTime_from();
        LocalTime newTimeTo = scheduleRequest.getTime_to();
        LocalTime existingTimeFrom = existingSchedule.getTime_from();
        LocalTime existingTimeTo = existingSchedule.getTime_to();

        if(existingSchedule.getScheduleType() == ScheduleType.WEEKLY) {
            if(existingSchedule.getWeekDay().equals(scheduleRequest.getWeekDay())) {
                return isTimeOverlapping(newTimeFrom, newTimeTo, existingTimeFrom, existingTimeTo);
            }
        }

        else if(existingSchedule.getScheduleType() == ScheduleType.ONCE) {
            if(existingSchedule.getEndDate().isAfter(existingSchedule.getStartDate().plusDays(6))) {
                return isTimeOverlapping(newTimeFrom, newTimeTo, existingTimeFrom, existingTimeTo);
            }
            else{
                for(LocalDate date = existingSchedule.getStartDate(); !date.isAfter(existingSchedule.getEndDate()); date = date.plusDays(1)) {
                    if(date.getDayOfWeek().toString().equals(scheduleRequest.getWeekDay().toString())) {
                        if(isTimeOverlapping(newTimeFrom, newTimeTo, existingTimeFrom, existingTimeTo)) {
                            return true;
                        }
                    }
                }
            }
        }

        else if(existingSchedule.getScheduleType() == ScheduleType.DAILY) {
            return isTimeOverlapping(newTimeFrom, newTimeTo, existingTimeFrom, existingTimeTo);
        }

        return false;
    }

    private boolean isScheduleInMonth(Schedule schedule, int month, int year) {
        switch (schedule.getScheduleType()) {
            case DAILY:
                return true;
            case WEEKLY:
                if (schedule.getWeekDay() == null) return false;

                // Loop through all days in month
                for (int day = 1; day <= YearMonth.of(year, month).lengthOfMonth(); day++) {
                    LocalDate date = LocalDate.of(year, month, day);

                    // Compare with your weekDay enum
                    if (date.getDayOfWeek().name().equals(schedule.getWeekDay().name())) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }
}
