package dadn_SmartHome.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dadn_SmartHome.dto.AuthenticateDTO.AuthenticateDTO;
import dadn_SmartHome.dto.AuthenticateDTO.IntrospectDTO;
import dadn_SmartHome.model.DataInfo;
import dadn_SmartHome.service.implement.DataInfoService;
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

    //DTO response
    DataInfo dataInfo;
    List<DataInfo> listData;

    //Encode device
    String encodedFeeds;
}
