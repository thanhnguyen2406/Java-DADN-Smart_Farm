package dadn_SmartFarm.service.interf;

import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.dto.UserDTO.UserDTO;

public interface IUserService {
    Response registerUser(UserDTO request);
}
