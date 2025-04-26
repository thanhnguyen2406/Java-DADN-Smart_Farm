package dadn_SmartFarm.controller;

import dadn_SmartFarm.dto.DeviceTriggerDTO.DeviceTriggerDTO;
import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.service.interf.IDeviceTriggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/triggers")
@RequiredArgsConstructor
public class DeviceTriggerController {
    private final IDeviceTriggerService deviceTriggerService;

    @PostMapping("/add")
    public ResponseEntity<Response> addTrigger(@RequestBody DeviceTriggerDTO deviceTriggerDTO) {
        Response response = deviceTriggerService.addTrigger(deviceTriggerDTO);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateTrigger(@RequestBody DeviceTriggerDTO deviceTriggerDTO) {
        Response response = deviceTriggerService.updateTrigger(deviceTriggerDTO);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteTrigger(@PathVariable("id") long id) {
        Response response = deviceTriggerService.deleteTrigger(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/device/{id}")
    public ResponseEntity<Response> getTriggersByDeviceId(
            @PathVariable("id") long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Response response = deviceTriggerService.getTriggersByDeviceId(id, pageable);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/room-sensor/{id}")
    public ResponseEntity<Response> getSensorFeedsByRoom(@PathVariable("id") long roomId) {
        Response response = deviceTriggerService.getSensorFeedsByRoom(roomId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/room-control/{id}")
    public ResponseEntity<Response> getControlFeedsByRoom(@PathVariable("id") long roomId) {
        Response response = deviceTriggerService.getControlFeedsByRoom(roomId);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
