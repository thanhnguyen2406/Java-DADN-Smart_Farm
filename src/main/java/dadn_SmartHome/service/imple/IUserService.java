package dadn_SmartHome.service.imple;

import com.nimbusds.jose.JOSEException;
import dadn_SmartHome.dto.Response;
import dadn_SmartHome.dto.UserDTO.UserDTO;

public interface IUserService {
    Response registerUser(UserDTO request);
}
