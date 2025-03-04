package dadn_SmartHome.mapper;

import dadn_SmartHome.dto.UserDTO.UserDTO;
import dadn_SmartHome.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toUser(UserDTO userDTO) {
        return User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .build();
    }
}
