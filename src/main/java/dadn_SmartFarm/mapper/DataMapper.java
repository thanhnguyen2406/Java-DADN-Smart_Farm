package dadn_SmartFarm.mapper;

import dadn_SmartFarm.dto.DataInfo.DataDTO;
import dadn_SmartFarm.dto.DataInfo.DataInfoDTO;
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
