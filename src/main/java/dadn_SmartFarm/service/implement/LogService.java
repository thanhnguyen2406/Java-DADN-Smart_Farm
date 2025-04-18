package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.dto.LogDTO.LogDTO;
import dadn_SmartFarm.mapper.LogMapper;
import dadn_SmartFarm.model.Log;
import dadn_SmartFarm.repository.LogRepository;
import dadn_SmartFarm.service.interf.ILogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LogService implements ILogService {
    LogRepository logRepository;
    LogMapper logMapper;

    @Override
    public void createLog(LogDTO logDTO) {
        Log log = logMapper.toLog(logDTO);
        log.setCreatedAt(LocalDateTime.now());
        logRepository.save(log);
    }
}
