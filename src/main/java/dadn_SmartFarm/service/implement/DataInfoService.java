package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.dto.DataInfo.DataDTO;
import dadn_SmartFarm.dto.DataInfo.DataInfoDTO;
import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.mapper.DataMapper;
import dadn_SmartFarm.model.DataInfo;
import dadn_SmartFarm.service.interf.IDataInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class DataInfoService implements IDataInfoService {
    private final DataMapper dataMapper;

    @Value("${ADAFRUIT_X_AIO_KEY}")
    private String aioKey;

    @Value("${ADAFRUIT_USERNAME}")
    private String adafruitUsername;

    @Override
    public Response getAllData(String url) {
        RestTemplate restTemplate = new RestTemplate();
        DataInfo[] feedArray = restTemplate.getForObject(url, DataInfo[].class);

        return Response.builder()
                        .code(200)
                        .message("Data fetch successfully")
                        .listData(Arrays.asList(feedArray))
                        .build();
    }

    @Override
    public Response getLatestData(String url) {
        RestTemplate restTemplate = new RestTemplate();
        DataInfo[] feedArray = restTemplate.getForObject(url, DataInfo[].class);

        return Response.builder()
                .code(200)
                .message("Data fetch successfully")
                .dataInfo(feedArray[0])
                .build();
    }

    @Override
    public Response sendData(DataDTO dataDTO) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-AIO-Key", aioKey);

        DataInfoDTO dataInfoDTO = dataMapper.toDataInfoDTO(dataDTO);

        String url = "https://io.adafruit.com/api/v2/" + adafruitUsername + "/feeds/" + dataDTO.getFeed_key() + "/data";
        HttpEntity<DataInfoDTO> request = new HttpEntity<>(dataInfoDTO, headers);
        restTemplate.postForEntity(url, request, DataInfo.class);
        return Response.builder()
                .code(200)
                .message("Data send successfully")
                .build();
    }
}
