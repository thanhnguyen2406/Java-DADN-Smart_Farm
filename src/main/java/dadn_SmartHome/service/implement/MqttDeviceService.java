package dadn_SmartHome.service.implement;

import dadn_SmartHome.service.interf.IMqttDeviceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MqttDeviceService implements IMqttDeviceService {
}
