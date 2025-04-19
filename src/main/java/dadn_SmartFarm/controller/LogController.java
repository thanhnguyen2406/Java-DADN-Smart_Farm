package dadn_SmartFarm.controller;

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

    //API get all log by ALL (theo tung TYPE -  1 trong 4), filter theo device

    @GetMapping("/device/{feeedKeey}")
    public ResponseEntity<Response> getLogByDeviceId(
            @PathVariable String feeedKeey,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Response response = logService.getLogByFeedKey(feeedKeey, pageable);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
