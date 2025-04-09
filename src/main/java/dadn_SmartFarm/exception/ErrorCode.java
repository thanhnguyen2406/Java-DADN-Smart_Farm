package dadn_SmartFarm.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    //401: Unauthenticated errors
    UNAUTHENTICATED(401, "Unauthenticated"),
    UNAUTHENTICATED_USERNAME_PASSWORD(401, "Please check username or password again"),
    UNAUTHENTICATED_USERNAME(401, "Please check your username again"),
    UNAUTHENTICATED_USERNAME_DOMAIN(401, "Please enter email ends with @gmail.com"),

    //400: Invalid encoded device
    ENCODED_DEVICE_INVALID(400, "Encoded device string is invalid"),

    //404: Resource not found errors
    USER_NOT_FOUND(404, "User not found"),
    DEVICE_NOT_FOUND(404, "Device not found"),
    TRIGGER_NOT_FOUND(404, "Trigger not found"),
    FEED_SENSOR_NOT_FOUND(404, "Feed sensor not found"),
    FEED_CONTROL_NOT_FOUND(404, "Feed control not found"),

    //409: Resource existed errors
    USER_EXISTED(409, "User already existed"),
    FEED_EXISTED(409, "Feed already existed"),
    TRIGGER_EXISTED(409, "Trigger already existed");

    int code;
    String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
