package dadn_SmartHome.controller;

import dadn_SmartHome.dto.DataInfo.DataDTO;
import dadn_SmartHome.dto.ResponseDTO.Response;
import dadn_SmartHome.service.interf.IDataInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataInfoController {

    @Autowired
    private IDataInfoService dataInfoService;

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
