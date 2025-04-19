package dadn_SmartFarm.service.interf;

import dadn_SmartFarm.dto.LogDTO.LogDTO;
import dadn_SmartFarm.dto.LogDTO.LogTypeDTO;
import dadn_SmartFarm.dto.Response;
import org.springframework.data.domain.Pageable;

public interface ILogService {
    void createLog(LogDTO logDTO);

    Response getLogByFeedKey(String feedKey, Pageable pageable);
    Response getLogByLogType(LogTypeDTO request, Pageable pageable);
    Response deleteLogById(Long id);
}
