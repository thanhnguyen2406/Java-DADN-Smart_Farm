package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.model.Device;
import dadn_SmartFarm.model.FeedInfo;
import dadn_SmartFarm.model.Statistic;
import dadn_SmartFarm.model.enums.DeviceType;
import dadn_SmartFarm.repository.DeviceRepository;
import dadn_SmartFarm.repository.StatisticRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SensorDataCollectorService {
    private final DeviceRepository deviceRepository;
    private final StatisticRepository statisticRepository;
    private final AdafruitClientService adafruitClientService;

    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void collectSensorData(){
        List<Device> sensorDevices = deviceRepository.findByType(DeviceType.SENSOR);
        log.info("Collecting sensor data from {} devices.", sensorDevices.size());

        for(Device device : sensorDevices){
            Map<String, FeedInfo> feedsList = device.getFeedsList();
            if(feedsList == null || feedsList.isEmpty()){
                log.warn("Device id={} có feedsList trống.", device.getId());
                continue;
            }

            for(Map.Entry<String, FeedInfo> entry : feedsList.entrySet()){
                FeedInfo feedInfo = entry.getValue();
                String feedKey = entry.getKey();
                if(feedInfo == null || feedInfo.getFeedId() == null){
                    continue;
                }

                try{
                    double currentValue = adafruitClientService.getFeedValue(feedKey);            // This place will use some logic to get the value of that feedId

                    Statistic statistic = Statistic.builder()
                            .feedId(feedInfo.getFeedId())
                            .value(currentValue)
                            .timeStamp(LocalDateTime.now())
                            .build();

                    statisticRepository.save(statistic);

                    log.info("Saved Statistic for feedId {}: value {} at {}",
                            feedInfo.getFeedId(), currentValue, LocalDateTime.now());
                } catch (Exception ex){
                    log.error("Error collecting data for feedId {}: {}",
                            feedInfo.getFeedId(), ex.getMessage());
                }
            }
        }
    }
}
