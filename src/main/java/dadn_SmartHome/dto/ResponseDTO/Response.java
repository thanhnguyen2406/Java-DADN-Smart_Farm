package dadn_SmartHome.dto.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import dadn_SmartHome.model.DataInfo;
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
