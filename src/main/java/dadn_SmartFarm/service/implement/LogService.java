package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.dto.LogDTO.LogDTO;
import dadn_SmartFarm.dto.LogDTO.LogTypeDTO;
import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.exception.AppException;
import dadn_SmartFarm.exception.ErrorCode;
import dadn_SmartFarm.mapper.LogMapper;
import dadn_SmartFarm.model.Log;
import dadn_SmartFarm.repository.DeviceRepository;
import dadn_SmartFarm.repository.LogRepository;
import dadn_SmartFarm.service.interf.ILogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LogService implements ILogService {
    LogRepository logRepository;
    DeviceRepository deviceRepository;
    LogMapper logMapper;

    @Override
    public void createLog(LogDTO logDTO) {
        Log log = logMapper.toLog(logDTO);
        log.setCreatedAt(LocalDateTime.now());
        logRepository.save(log);
    }

    @Override
    public Response getLogByFeedKey(String feedKey, Pageable pageable) {
        try {
            if (deviceRepository.existsFeedKey(feedKey) == 0) {
                throw new AppException(ErrorCode.FEED_SENSOR_NOT_FOUND);
            }
            Page<Log> logPage = logRepository.findByFeedKey(feedKey, pageable);
            if (logPage.isEmpty()) {
                return Response.builder()
                        .code(200)
                        .message("No logs are found")
                        .build();
            }

            List<LogDTO> logDTOList = logPage.getContent().stream().map(logMapper::toLogDTO).toList();
            return Response.builder()
                    .code(200)
                    .message("All logs fetched successfully")
                    .listLogDTO(logDTOList)
                    .currentPage(logPage.getNumber())
                    .totalElements((int) logPage.getTotalElements())
                    .totalPages(logPage.getTotalPages())
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while fetching logs: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getLogByLogType(LogTypeDTO request, Pageable pageable) {
        try {
            if (deviceRepository.existsFeedKey(request.getFeedKey()) == 0) {
                throw new AppException(ErrorCode.FEED_SENSOR_NOT_FOUND);
            }
            Page<Log> logPage = logRepository.findByFeedKeyAndLogType(request.getFeedKey(), request.getLogType(),pageable);
            if (logPage.isEmpty()) {
                return Response.builder()
                        .code(200)
                        .message("No logs are found")
                        .build();
            }

            List<LogDTO> logDTOList = logPage.getContent().stream().map(logMapper::toLogDTO).toList();
            return Response.builder()
                    .code(200)
                    .message("All logs fetched successfully")
                    .listLogDTO(logDTOList)
                    .currentPage(logPage.getNumber())
                    .totalElements((int) logPage.getTotalElements())
                    .totalPages(logPage.getTotalPages())
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while fetching logs: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response deleteLogById(Long id) {
        try {
            Log log = logRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.LOG_NOT_FOUND));
            logRepository.delete(log);
            return Response.builder()
                    .code(200)
                    .message("Log deleted successfully")
                    .build();
        } catch (AppException e) {
            return Response.builder()
                    .code(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(500)
                    .message("Error while deleting logs: " + e.getMessage())
                    .build();
        }
    }
}
