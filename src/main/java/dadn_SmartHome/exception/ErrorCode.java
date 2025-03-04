package dadn_SmartHome.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    //400: Unauthenticated errors
    UNAUTHENTICATED(401, "Unauthenticated"),
    UNAUTHENTICATED_USERNAME_PASSWORD(401, "Please check username or password again"),
    UNAUTHENTICATED_USERNAME(401, "Please check your username again"),

    //404: Not found errors
    USER_NOT_FOUND(400, "User not found");

    int code;
    String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
