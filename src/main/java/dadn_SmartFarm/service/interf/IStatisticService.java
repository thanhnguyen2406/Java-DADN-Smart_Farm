package dadn_SmartFarm.service.interf;

import dadn_SmartFarm.dto.StatisticDTO.StatisticResponseDTO.StatisticResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IStatisticService {
    StatisticResponse getStatistic(long feedId, LocalDate date);
}
