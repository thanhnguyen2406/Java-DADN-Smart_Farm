package dadn_SmartHome.dto.AuthenticateDTO;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticateDTO {
    String username;
    String password;
}
