package dadn_SmartFarm.controller;

import dadn_SmartFarm.dto.DeviceDTO.DeviceDTO;
import dadn_SmartFarm.dto.DeviceDTO.DeviceRoomDTO;
import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.service.interf.IDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final IDeviceService deviceService;

    @PostMapping("/add")
    public ResponseEntity<Response> addDevice(@RequestBody DeviceDTO request) {
        Response response = deviceService.addDevice(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateDevice(@RequestBody DeviceDTO request) {
        Response response = deviceService.updateDevice(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteDevice(@PathVariable("id") long id) {
        Response response = deviceService.deleteDevice(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/assign-room")
    public ResponseEntity<Response> assignDeviceToRoom(@RequestBody DeviceRoomDTO request) {
        Response response = deviceService.assignDeviceToRoom(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/dismiss-room/{id}")
    public ResponseEntity<Response> dismissDeviceToRoom(@PathVariable long id) {
        Response response = deviceService.dismissDeviceToRoom(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<Response> getDevicesByRoomId(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Response response = deviceService.getDevicesByRoomId(id, pageable);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/unassign")
    public ResponseEntity<Response> getUnassignedDevices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "7") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Response response = deviceService.getUnassignedDevices(pageable);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
