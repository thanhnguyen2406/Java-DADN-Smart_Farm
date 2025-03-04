package dadn_SmartHome.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    //Unauthenticated
    UNAUTHENTICATED_USERNAME_PASSWORD(401, "Please check email or password again"),
    UNAUTHENTICATED_USERNAME(401, "Please check email again");

    int code;
    String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
