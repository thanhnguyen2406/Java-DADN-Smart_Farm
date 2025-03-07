package dadn_SmartHome.mapper;

import dadn_SmartHome.dto.DataInfo.DataDTO;
import dadn_SmartHome.dto.DataInfo.DataInfoDTO;
import dadn_SmartHome.model.DataInfo;
import org.springframework.stereotype.Component;

@Component
public class DataMapper {
    public DataInfoDTO toDataInfoDTO(DataDTO dataDTO) {
        return DataInfoDTO.builder()
                .value(dataDTO.getValue())
                .feed_id(dataDTO.getFeed_id())
                .feed_key(dataDTO.getFeed_key())
                .build();
    }
}
