package dadn_SmartHome.controller;

import com.nimbusds.jose.JOSEException;
import dadn_SmartHome.dto.AuthenticateDTO.AuthenticateDTO;
import dadn_SmartHome.dto.AuthenticateDTO.IntrospectDTO;
import dadn_SmartHome.dto.Response;
import dadn_SmartHome.service.interf.IAuthenticateService;
import dadn_SmartHome.service.interf.IDataInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticateController {
    private final IAuthenticateService authenticateService;

    @PostMapping("/login")
    public ResponseEntity<Response> authenticate(@RequestBody AuthenticateDTO request) {
        Response response = authenticateService.authenticate(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/introspect")
    public ResponseEntity<Response> introspect(@RequestBody IntrospectDTO request) throws ParseException, JOSEException {
        Response response = authenticateService.introspect(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
