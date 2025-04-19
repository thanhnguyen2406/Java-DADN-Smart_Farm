package dadn_SmartFarm.utils;

import dadn_SmartFarm.service.implement.DeviceService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

public class MqttStartupListener implements ApplicationListener<ApplicationReadyEvent> {
    DeviceService deviceService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        deviceService.initActiveDevices();
    }
}
