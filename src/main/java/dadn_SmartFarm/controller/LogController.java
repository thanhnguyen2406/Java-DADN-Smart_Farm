package dadn_SmartFarm.controller;

import dadn_SmartFarm.dto.LogDTO.LogDTO;
import dadn_SmartFarm.dto.LogDTO.LogTypeDTO;
import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.service.interf.ILogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {
    private final ILogService logService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        Response response = logService.deleteLogById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/feed/{feedKey}")
    public ResponseEntity<Response> getLogByFeedKey(
            @PathVariable String feedKey,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Response response = logService.getLogByFeedKey(feedKey, pageable);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/type")
    public ResponseEntity<Response> getLogByDeviceType(
            @RequestBody LogTypeDTO request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Response response = logService.getLogByLogType(request, pageable);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
