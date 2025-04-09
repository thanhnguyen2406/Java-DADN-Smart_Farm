package dadn_SmartFarm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dadn_SmartFarm.model.DataInfo;
import dadn_SmartFarm.model.DeviceTrigger;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    int code;
    String message;
    String token;
    boolean authenticated;

    Integer currentPage;
    Integer totalPages;
    Integer totalElements;

    //DTO response
    DataInfo dataInfo;
    List<DataInfo> listData;
    List<DeviceTrigger> listDeviceTriggers;

    //Encode device
    String encodedFeeds;
}
