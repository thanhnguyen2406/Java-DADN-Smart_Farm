package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.dto.UserDTO.UserDTO;
import dadn_SmartFarm.exception.AppException;
import dadn_SmartFarm.exception.ErrorCode;
import dadn_SmartFarm.mapper.UserMapper;
import dadn_SmartFarm.model.User;
import dadn_SmartFarm.repository.UserRepository;
import dadn_SmartFarm.service.interf.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService implements IUserService {
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public Response registerUser(UserDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        String sanitizedUsername = request.getEmail().trim();
        if (!sanitizedUsername.endsWith("@gmail.com")) {
            throw new AppException(ErrorCode.UNAUTHENTICATED_USERNAME_DOMAIN);
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
