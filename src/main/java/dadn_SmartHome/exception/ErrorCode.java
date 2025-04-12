package dadn_SmartHome.exception;

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
    ROOM_NOT_FOUND(404, "Room not found"),
    SCHEDULE_NOT_FOUND(404, "Schedule not found"),

    //409: Resource existed errors
    USER_EXISTED(409, "User already existed"),
    FEED_EXISTED(409, "Feed already existed"),
    ROOM_EXISTED(409, "Room already existed"),

    //4002: Bad Request / logic conflict
    START_DATE_AFTER_END_DATE(4002, "Start date after end date"),
    REQUEST_START_DATE_AND_END_DATE(4002, "Request start date and end date"),
    REQUEST_START_TIME_BEFORE_END_TIME(4002, "Request start time before end time"),
    REQUEST_WEEKDAY(4002, "Request weekday"),
    SCHEDULE_TIME_OVERLAP(4002, "Schedule time overlap");

    int code;
    String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
