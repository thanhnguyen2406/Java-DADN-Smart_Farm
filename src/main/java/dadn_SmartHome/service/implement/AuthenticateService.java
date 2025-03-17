package dadn_SmartHome.service.implement;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dadn_SmartHome.dto.AuthenticateDTO.AuthenticateDTO;
import dadn_SmartHome.dto.AuthenticateDTO.IntrospectDTO;
import dadn_SmartHome.dto.Response;
import dadn_SmartHome.exception.AppException;
import dadn_SmartHome.exception.ErrorCode;
import dadn_SmartHome.model.User;
import dadn_SmartHome.repository.UserRepository;
import dadn_SmartHome.service.interf.IAuthenticateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateService implements IAuthenticateService {
    private final UserRepository userRepository;

    @Value("${JWT_SIGNER_KEY}")
    private String signerKey;

    @Override
    public Response authenticate(AuthenticateDTO request) {
        String sanitizedUsername = request.getEmail().trim();

        User user = userRepository.findByEmail(sanitizedUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(5);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = generateToken(user);

        return Response.builder()
                .code(200)
                .message("Login successfully")
                .authenticated(authenticated)
                .token(token)
                .build();
    }

    @Override
    public Response introspect(IntrospectDTO request) throws ParseException, JOSEException {
        String token = request.getToken();
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);

        String message = (verified && expirationTime.after(new Date())) ? "Token is valid" : "Token is invalid";

        return Response.builder()
                .code(200)
                .message(message)
                .token(null)
                .build();
    }

    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("SmartHomeApp")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(2, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
}
