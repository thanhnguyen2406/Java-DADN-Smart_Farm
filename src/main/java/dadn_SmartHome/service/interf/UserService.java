package dadn_SmartHome.service.interf;

import dadn_SmartHome.dto.Response;
import dadn_SmartHome.dto.UserDTO.UserDTO;
import dadn_SmartHome.exception.AppException;
import dadn_SmartHome.exception.ErrorCode;
import dadn_SmartHome.mapper.UserMapper;
import dadn_SmartHome.model.User;
import dadn_SmartHome.repository.UserRepository;
import dadn_SmartHome.service.imple.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Response registerUser(UserDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(5);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return Response.builder()
                .code(200)
                .message("Add user successfully")
                .build();
    }
}
