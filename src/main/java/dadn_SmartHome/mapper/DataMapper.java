package dadn_SmartHome.mapper;

import dadn_SmartHome.dto.DataInfo.DataDTO;
import dadn_SmartHome.dto.DataInfo.DataInfoDTO;
import dadn_SmartHome.model.DataInfo;
import org.springframework.stereotype.Component;

@Component
public class DataMapper {
    public DataInfoDTO toDataInfoDTO(DataDTO dto) {
        return DataInfoDTO.builder()
                .value(dto.getValue())
                .feed_id(dto.getFeed_id())
                .feed_key(dto.getFeed_key())
                .build();
    }
}
