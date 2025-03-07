package dadn_SmartHome.service.implement;

import dadn_SmartHome.service.interf.IMQTTDeviceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MQTTDeviceService implements IMQTTDeviceService {
}
