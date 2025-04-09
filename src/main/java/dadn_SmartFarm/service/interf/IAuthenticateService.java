package dadn_SmartFarm.service.interf;

import com.nimbusds.jose.JOSEException;
import dadn_SmartFarm.dto.AuthenticateDTO.AuthenticateDTO;
import dadn_SmartFarm.dto.AuthenticateDTO.IntrospectDTO;
import dadn_SmartFarm.dto.Response;

import java.text.ParseException;

public interface IAuthenticateService {
    Response authenticate(AuthenticateDTO request);
    Response introspect(IntrospectDTO request) throws ParseException, JOSEException;
}
