package dadn_SmartFarm.controller;

import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.dto.UserDTO.UserDTO;
import dadn_SmartFarm.service.interf.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping("/register")
    ResponseEntity<Response> registerUser(@RequestBody UserDTO request) {
        Response response = userService.registerUser(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

}
