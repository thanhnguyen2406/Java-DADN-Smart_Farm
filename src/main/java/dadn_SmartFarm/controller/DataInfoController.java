package dadn_SmartFarm.controller;

import dadn_SmartFarm.dto.DataInfo.DataDTO;
import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.service.interf.IDataInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataInfoController {
    private final IDataInfoService dataInfoService;

    @GetMapping("/all")
    public ResponseEntity<Response> getAllData(@RequestHeader String url) {
        Response response = dataInfoService.getAllData(url);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/latest")
    public ResponseEntity<Response> getLatestData(@RequestHeader String url) {
        Response response = dataInfoService.getLatestData(url);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/send")
    public ResponseEntity<Response> sendData(@RequestBody DataDTO dataDTO) {
        Response response = dataInfoService.sendData(dataDTO);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
