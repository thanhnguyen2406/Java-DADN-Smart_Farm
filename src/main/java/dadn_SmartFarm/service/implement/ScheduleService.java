package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.dto.DeviceDTO.DeviceDTO;
import dadn_SmartFarm.dto.ScheduleDTO.ScheduleRequestDTO.CreateScheduleRequest;
import dadn_SmartFarm.dto.ScheduleDTO.ScheduleResponse.CreateScheduleResponse;
import dadn_SmartFarm.dto.ScheduleDTO.ScheduleResponse.DeleteScheduleResponse;
import dadn_SmartFarm.dto.ScheduleDTO.ScheduleResponse.GetScheduleResponse;
import dadn_SmartFarm.exception.AppException;
import dadn_SmartFarm.exception.ErrorCode;
import dadn_SmartFarm.model.Device;
import dadn_SmartFarm.model.FeedInfo;
import dadn_SmartFarm.model.Schedule;
import dadn_SmartFarm.model.enums.ScheduleType;
import dadn_SmartFarm.model.enums.Status;
import dadn_SmartFarm.repository.DeviceRepository;
import dadn_SmartFarm.repository.RoomRepository;
import dadn_SmartFarm.repository.ScheduleRepository;
import dadn_SmartFarm.service.interf.IScheduleService;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleService implements IScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final RoomRepository roomRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceService deviceService;


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

        if(!deviceService.checkFeedIdExists(device.getId(), createScheduleRequest.getFeedId())){
            throw new AppException(ErrorCode.FEED_ID_NOT_FOUND);
        }

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
                .feedId(createScheduleRequest.getFeedId())
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

        // Check if the schedule falls within the current month and add to active schedules if it does
        addToActiveSchedulesIfCurrentMonth(schedule);

        // Return result
        return CreateScheduleResponse.builder()
                .code(200)
                .message("Success")
                .authenticated(true)
                .id_device(schedule.getDevice().getId())
                .feedId(schedule.getFeedId())
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

        // Remove the schedule from activeSchedules if present
        removeFromActiveSchedules(id);

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
            return isTimeOverlapping(newTimeFrom, newTimeTo, existingSchedule.getTime_from(), existingSchedule.getTime_to());
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

    /**
     * Inner class to represent a schedule with its execution information
     */
    @Data
    @Builder
    static class inforTask {
        boolean start;
        LocalDateTime localDateTime;
    }

    /**
     * Inner class to represent a schedule with its execution information
     */
    @Data
    @Builder
    static class ScheduleTask {
        Schedule schedule;
        Status startStatus;
        Status endStatus;
        List<inforTask> executionTimes;
    }

    @PostConstruct
    public void init() {
        try {
            log.info("Initializing ScheduleService");
            loadSchedulesForCurrentPeriod();
            log.info("ScheduleService initialization complete");
        } catch (Exception e) {
            log.error("Error during ScheduleService initialization", e);
            throw e; // Re-throw to let Spring know initialization failed
        }
    }

    // List to store active schedules that need to be checked
    final List<ScheduleTask> activeSchedules = new ArrayList<>();

    // Track the current month for reloading purposes
    int currentMonth = LocalDate.now().getMonthValue();
    int currentYear = LocalDate.now().getYear();

    // Threshold for reloading schedules when list gets small
    static final int RELOAD_THRESHOLD = 5;

    /**
     * Loads all schedules for the current month and prepares them for execution
     */
    private void loadSchedulesForCurrentPeriod() {
        log.info("Loading schedules for {}/{}", currentMonth, currentYear);
        activeSchedules.clear();

        LocalDate startOfMonth = LocalDate.of(currentYear, currentMonth, 1);
        LocalDate endOfMonth = YearMonth.of(currentYear, currentMonth).atEndOfMonth();
        LocalDate now = LocalDate.now();

        // Use start date as the latter of now or start of month
        LocalDate effectiveStartDate = now.isBefore(startOfMonth) ? startOfMonth : now;

        // Query only relevant schedules instead of findAll()
        List<Schedule> relevantSchedules = scheduleRepository.findSchedulesForPeriod(effectiveStartDate, endOfMonth);

        for (Schedule schedule : relevantSchedules) {
            List<inforTask> executionTimes = calculateExecutionTimes(schedule, effectiveStartDate, endOfMonth);

            // Only add schedules that have execution times in the current period
            if (!executionTimes.isEmpty()) {
                activeSchedules.add(ScheduleTask.builder()
                        .schedule(schedule)
                        .startStatus(schedule.getStatus())
                        .endStatus(schedule.getStatus().opposite())
                        .executionTimes(executionTimes)
                        .build());
            }
        }

        log.info("Loaded {} schedules for execution", activeSchedules.size());
    }

    /**
     * Calculate all times when a schedule should execute between start and end dates
     */
    private List<inforTask> calculateExecutionTimes(Schedule schedule, LocalDate startDate, LocalDate endDate) {
        List<inforTask> executionTimes = new ArrayList<>();

        switch (schedule.getScheduleType()) {
            case ONCE:
                // For ONCE schedules, check if the schedule's start date falls within our date range
                if (schedule.getStartDate() != null) {
                    LocalDate scheduleStartDate = schedule.getStartDate();
                    LocalDate scheduleEndDate = schedule.getEndDate();

                    // Determine the effective start date (later of schedule start date and method start date)
                    LocalDate effectiveStartDate = scheduleStartDate.isBefore(startDate) ? startDate : scheduleStartDate;
                    LocalDate effectiveEndDate = scheduleEndDate.isAfter(endDate) ? endDate : scheduleEndDate;

                    // For each date from the effective start date to the end date
                    for (LocalDate date = effectiveStartDate; !date.isAfter(effectiveEndDate); date = date.plusDays(1)) {
                        // Add time_from execution time
                        if (schedule.getTime_from() != null) {
                            executionTimes.add(inforTask.builder()
                                            .start(true)
                                            .localDateTime(LocalDateTime.of(date, schedule.getTime_from()))
                                    .build());
                        }

                        // Add time_to execution time
                        if (schedule.getTime_to() != null) {
                            executionTimes.add(inforTask.builder()
                                            .start(false)
                                            .localDateTime(LocalDateTime.of(date, schedule.getTime_to()))
                                    .build());
                        }
                    }
                }
                break;

            case WEEKLY:
                if (schedule.getWeekDay() != null) {
                    // For each date in our range
                    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                        // If this date matches the weekday of the schedule
                        if (date.getDayOfWeek().name().equals(schedule.getWeekDay().name())) {
                            // Add time_from execution time
                            if (schedule.getTime_from() != null) {
                                executionTimes.add(inforTask.builder()
                                                .start(true)
                                                .localDateTime(LocalDateTime.of(date, schedule.getTime_from()))
                                        .build());
                            }

                            // Add time_to execution time
                            if (schedule.getTime_to() != null) {
                                executionTimes.add(inforTask.builder()
                                                .start(false)
                                                .localDateTime(LocalDateTime.of(date, schedule.getTime_to()))
                                        .build());
                            }
                        }
                    }
                }
                break;

            case DAILY:
                // For DAILY schedules, add execution times for each day in the range
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    // Add time_from execution time
                    if (schedule.getTime_from() != null) {
                        executionTimes.add(inforTask.builder()
                                        .start(true)
                                        .localDateTime(LocalDateTime.of(date, schedule.getTime_from()))
                                .build());
                    }

                    // Add time_to execution time
                    if (schedule.getTime_to() != null) {
                        executionTimes.add(inforTask.builder()
                                        .start(false)
                                        .localDateTime(LocalDateTime.of(date, schedule.getTime_to()))
                                .build());
                    }
                }
                break;
        }

        return executionTimes;
    }

    /**
     * Check schedules every minute
     */
    @Scheduled(fixedRate = 60000) // Check every minute
    private void checkSchedules() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Schedule check at: {}", now);

        // Check if we need to reload for a new month
        if (now.getMonthValue() != currentMonth || now.getYear() != currentYear) {
            currentMonth = now.getMonthValue();
            currentYear = now.getYear();
            loadSchedulesForCurrentPeriod();
            return;
        }

        // Create a window of +/- 30 seconds to allow for slight timing differences
        LocalDateTime windowStart = now.minusSeconds(30);
        LocalDateTime windowEnd = now.plusSeconds(30);

        // Track schedules to be updated in the active list
        List<ScheduleTask> tasksToRemove = new ArrayList<>();

        // Check each schedule
        for (ScheduleTask task : activeSchedules) {
            // Find execution times that fall within our current time window
            List<inforTask> matchingTimes = task.getExecutionTimes().stream()
                    .filter(time -> !time.getLocalDateTime().isBefore(windowStart) && !time.getLocalDateTime().isAfter(windowEnd))
                    .toList();

            if (!matchingTimes.isEmpty()) {
                // We found a schedule that should execute now
                Schedule schedule = task.getSchedule();

                // Execute each matching time (there might be multiple in rare cases)
                for (inforTask timeInfo : matchingTimes) {
                    // Determine which status to apply based on whether it's a start or end time
                    Status targetStatus = timeInfo.isStart() ? task.getStartStatus() : task.getEndStatus();

                    log.info("Executing schedule: ID={}, Type={}, Device={}, Action={}, Target Status={}",
                            schedule.getId(), schedule.getScheduleType(),
                            schedule.getDevice().getId(),
                            timeInfo.isStart() ? "START" : "END",
                            targetStatus);

                    // Execute the schedule with the appropriate status
                    executeSchedule(schedule, targetStatus);
                }

                // Remove the executed times from the list
                task.getExecutionTimes().removeAll(matchingTimes);

                // If no more execution times, mark for removal
                if (task.getExecutionTimes().isEmpty()) {
                    tasksToRemove.add(task);
                }
            }
        }

        // Remove completed tasks
        activeSchedules.removeAll(tasksToRemove);

        // Check if we need to reload schedules (list getting small)
        if (activeSchedules.size() <= RELOAD_THRESHOLD) {
            // If we're near the end of month, load next month's schedules
            if (now.getDayOfMonth() >= 25) {
                // Calculate next month
                YearMonth nextMonth = YearMonth.of(currentYear, currentMonth).plusMonths(1);
                currentYear = nextMonth.getYear();
                currentMonth = nextMonth.getMonthValue();
                loadSchedulesForCurrentPeriod();
            } else {
                // Otherwise just reload the current month
                loadSchedulesForCurrentPeriod();
            }
        }
    }

    /**
     * Checks if the schedule falls within the current month and adds it to active schedules if it does
     */
    private void addToActiveSchedulesIfCurrentMonth(Schedule schedule) {
        try {
            YearMonth currentYearMonth = YearMonth.now();
            boolean isCurrentMonth = false;

            switch (schedule.getScheduleType()) {
                case ONCE:
                    // For ONCE schedules, check if startDate is in current month
                    LocalDate startDate = schedule.getStartDate();
                    if (startDate != null) {
                        YearMonth scheduleYearMonth = YearMonth.from(startDate);
                        isCurrentMonth = scheduleYearMonth.equals(currentYearMonth);
                    }
                    break;

                case DAILY:
                    // DAILY schedules are always active in the current month
                    isCurrentMonth = true;
                    break;

                case WEEKLY:
                    // WEEKLY schedules are always active in the current month if they have a weekDay
                    if (schedule.getWeekDay() != null) {
                        isCurrentMonth = true;
                    }
                    break;
            }

            if (isCurrentMonth) {
                log.info("Adding new schedule (ID: {}) to active schedules as it falls within the current month",
                        schedule.getId());

                // Calculate execution times for the current month
                LocalDate now = LocalDate.now();
                LocalDate endOfMonth = currentYearMonth.atEndOfMonth();
                List<inforTask> executionTimes = calculateExecutionTimes(schedule, now, endOfMonth);

                // Create a ScheduleTask and add it to activeSchedules
                if (!executionTimes.isEmpty()) {
                    ScheduleTask task = ScheduleTask.builder()
                            .schedule(schedule)
                            .startStatus(schedule.getStatus())
                            .endStatus(schedule.getStatus().opposite())
                            .executionTimes(executionTimes)
                            .build();

                    activeSchedules.add(task);

                    log.info("Added schedule with {} execution times", executionTimes.size());
                } else {
                    log.info("Schedule has no execution times in the current month");
                }
            } else {
                log.info("New schedule (ID: {}) does not fall within the current month, not adding to active schedules",
                        schedule.getId());
            }
        } catch (Exception e) {
            log.error("Error checking if schedule falls within current month: {}", e.getMessage(), e);
        }
    }

    /**
     * Execute a schedule - this would contain your actual execution logic
     */
    private void executeSchedule(Schedule schedule, Status targetStatus) {
        // Here you would implement the actual logic to execute the schedule
        // For example, sending a command to the device
        log.info("Device {} status changed to: {}",
                schedule.getDevice().getId(), targetStatus);

        Map<String, FeedInfo> feedList = schedule.getDevice().getFeedsList().entrySet().stream()
                .filter(entry -> schedule.getFeedId() == entry.getValue().getFeedId())
                .findFirst()
                .map(entry -> Map.of(entry.getKey(), entry.getValue()))
                .orElse(Map.of());

        deviceService.updateDevice(DeviceDTO.builder()
                        .id(schedule.getDevice().getId())
                        .roomId(schedule.getDevice().getRoomId())
                        .name(schedule.getDevice().getName())
                        .type(schedule.getDevice().getType())
                        .status(targetStatus)
                        .feedsList(feedList)
                .build());
    }

    /**
     * Helper method to remove a schedule from activeSchedules by its ID
     */
    private void removeFromActiveSchedules(long scheduleId) {
        // Create a copy of the list to avoid ConcurrentModificationException
        List<ScheduleTask> tasksToRemove = activeSchedules.stream()
                .filter(task -> task.getSchedule().getId() == scheduleId)
                .toList();

        if (!tasksToRemove.isEmpty()) {
            activeSchedules.removeAll(tasksToRemove);
            log.info("Removed schedule ID {} from active schedules", scheduleId);
        }
    }

}