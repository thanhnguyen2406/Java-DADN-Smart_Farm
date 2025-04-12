package dadn_SmartFarm.controller;

import dadn_SmartFarm.dto.ScheduleDTO.ScheduleRequestDTO.CreateScheduleRequest;
import dadn_SmartFarm.dto.ScheduleDTO.ScheduleResponse.CreateScheduleResponse;
import dadn_SmartFarm.dto.ScheduleDTO.ScheduleResponse.DeleteScheduleResponse;
import dadn_SmartFarm.dto.ScheduleDTO.ScheduleResponse.GetScheduleResponse;
import dadn_SmartFarm.service.implement.ScheduleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping()
    @Validated
    public GetScheduleResponse getSchedule(
            @RequestParam @Min(1) @Max(12) int month,
            @RequestParam int year,
            @RequestParam("id_room") long idRoom
    ){
        return scheduleService.GetListWork(month, year, idRoom);
    }

    @PostMapping("/create")
    public ResponseEntity<CreateScheduleResponse> createSchedule(@RequestBody @Valid CreateScheduleRequest createScheduleRequest) {
        // Gọi service để tạo schedule và nhận về response
        CreateScheduleResponse response = scheduleService.CreateSchedule(createScheduleRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{scheduleId}")
    public DeleteScheduleResponse deleteSchedule(@PathVariable long scheduleId) {
        return scheduleService.DeleteSchedule(scheduleId);
    }
}
