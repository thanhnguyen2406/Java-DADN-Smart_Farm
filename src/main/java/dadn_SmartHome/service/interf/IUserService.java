package dadn_SmartHome.service.interf;

import dadn_SmartHome.dto.ResponseDTO.Response;
import dadn_SmartHome.dto.UserDTO.UserDTO;

public interface IUserService {
    Response registerUser(UserDTO request);
}
