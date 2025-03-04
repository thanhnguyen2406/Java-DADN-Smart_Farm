package dadn_SmartHome.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Response {
    int code;
    String message;
    String token;
    boolean authenticated;

    //DTO response

}
