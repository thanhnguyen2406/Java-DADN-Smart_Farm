package dadn_SmartHome.service.imple;

import com.nimbusds.jose.JOSEException;
import dadn_SmartHome.dto.AuthenticateDTO.AuthenticateDTO;
import dadn_SmartHome.dto.AuthenticateDTO.IntrospectDTO;
import dadn_SmartHome.dto.Response;

import java.text.ParseException;

public interface IAuthenticateService {
    Response authenticate(AuthenticateDTO request);
    Response introspect(IntrospectDTO request) throws ParseException, JOSEException;
}
