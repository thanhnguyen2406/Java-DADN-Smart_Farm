package dadn_SmartHome.controller;

import dadn_SmartHome.dto.Response;
import dadn_SmartHome.dto.UserDTO.UserDTO;
import dadn_SmartHome.model.User;
import dadn_SmartHome.repository.UserRepository;
import dadn_SmartHome.service.imple.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
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
