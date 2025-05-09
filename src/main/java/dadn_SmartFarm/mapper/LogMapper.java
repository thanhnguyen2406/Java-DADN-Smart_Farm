package dadn_SmartFarm.mapper;

import dadn_SmartFarm.dto.LogDTO.LogDTO;
import dadn_SmartFarm.model.Log;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LogMapper {
    public Log toLog (LogDTO logDTO) {
        return Log.builder()
                .logType(logDTO.getLogType())
                .feedKey(logDTO.getFeedKey())
                .value(logDTO.getValue())
                .createdAt(logDTO.getCreatedAt())
                .build();
    }

    public LogDTO toLogDTO (Log log) {
        return LogDTO.builder()
                .id(log.getId())
                .logType(log.getLogType())
                .feedKey(log.getFeedKey())
                .value(log.getValue())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
