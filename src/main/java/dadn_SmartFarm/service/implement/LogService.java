package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.dto.DeviceDTO.DeviceDTO;
import dadn_SmartFarm.dto.LogDTO.LogDTO;
import dadn_SmartFarm.dto.Response;
import dadn_SmartFarm.exception.AppException;
import dadn_SmartFarm.exception.ErrorCode;
import dadn_SmartFarm.mapper.LogMapper;
import dadn_SmartFarm.model.Device;
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
//        try {
//            if (!deviceRepository.existsByFeedsKeyIn(id)) {
//                throw new AppException(ErrorCode.DEVICE_NOT_FOUND);
//            }
//            Page<Log> logPage = logRepository.findByFeedKey(id, pageable);
//            if (devicePage.isEmpty()) {
//                return Response.builder()
//                        .code(200)
//                        .message("No devices found")
//                        .build();
//            }
//
//            List<DeviceDTO> deviceList = devicePage.getContent().stream().map(deviceMapper::toDeviceDTO).toList();
//            return Response.builder()
//                    .code(200)
//                    .message("All devices of rooms fetched successfully")
//                    .listDeviceDTO(deviceList)
//                    .currentPage(devicePage.getNumber())
//                    .totalElements((int) devicePage.getTotalElements())
//                    .totalPages(devicePage.getTotalPages())
//                    .build();
//        } catch (AppException e) {
//            return Response.builder()
//                    .code(e.getErrorCode().getCode())
//                    .message(e.getErrorCode().getMessage())
//                    .build();
//        } catch (Exception e) {
//            return Response.builder()
//                    .code(500)
//                    .message("Error while fetching device from room: " + e.getMessage())
//                    .build();
//        }
        return null;
    }
}
