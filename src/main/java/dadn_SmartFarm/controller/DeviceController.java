package dadn_SmartHome.controller;

import dadn_SmartHome.dto.DeviceDTO.DeviceDTO;
import dadn_SmartHome.dto.Response;
import dadn_SmartHome.service.interf.IDeviceService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/encode/{id}")
    public ResponseEntity<Response> assignDevice(@PathVariable("id") long id) {
        Response response = deviceService.encodeDevice(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/assign")
    public ResponseEntity<Response> assignDevice(@RequestParam String encodedFeeds) {
        Response response = deviceService.assignDevice(encodedFeeds);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
