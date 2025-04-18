package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.model.Device;
import dadn_SmartFarm.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdafruitClientService {
    private final DeviceRepository deviceRepository;

    @Value("${ADAFRUIT_X_AIO_KEY}")
    private String aioKey;

    @Value("${ADAFRUIT_USERNAME}")
    private String adafruitUsername;

    public double getFeedValue(String feedKey){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-AIO-Key", aioKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://io.adafruit.com/api/v2/" + adafruitUsername + "/feeds/" + feedKey + "/data/last";
        RestTemplate restTemplate = new RestTemplate();

        try{
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object valueObj = response.getBody().get("value");
                if (valueObj != null) {
                    return Double.parseDouble(valueObj.toString());
                }
            }
        } catch(Exception e){
            log.error("Error getting feed value: " + e.getMessage());
        }

        return 0.0;
    }
}
