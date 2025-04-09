package dadn_SmartFarm.mapper;

import dadn_SmartFarm.dto.UserDTO.UserDTO;
import dadn_SmartFarm.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toUser(UserDTO dto) {
        return User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .name(dto.getName())
                .build();
    }
}
