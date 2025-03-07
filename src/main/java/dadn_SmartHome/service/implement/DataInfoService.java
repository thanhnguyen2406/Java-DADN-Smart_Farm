package dadn_SmartHome.service.implement;

import dadn_SmartHome.dto.DataInfo.DataDTO;
import dadn_SmartHome.dto.DataInfo.DataInfoDTO;
import dadn_SmartHome.dto.Response;
import dadn_SmartHome.mapper.DataMapper;
import dadn_SmartHome.model.DataInfo;
import dadn_SmartHome.service.interf.IDataInfoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

    protected String X_AIO_KEY = System.getProperty("X_AIO_KEY");

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
        headers.set("X-AIO-Key", X_AIO_KEY);

        DataInfoDTO dataInfoDTO = dataMapper.toDataInfoDTO(dataDTO);

        HttpEntity<DataInfoDTO> request = new HttpEntity<>(dataInfoDTO, headers);
        restTemplate.postForEntity(dataDTO.getUrl(), request, DataInfo.class);
        return Response.builder()
                .code(200)
                .message("Data send successfully")
                .build();
    }
}
