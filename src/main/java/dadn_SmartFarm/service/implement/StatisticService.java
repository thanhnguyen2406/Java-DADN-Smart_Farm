package dadn_SmartFarm.service.implement;

import dadn_SmartFarm.dto.StatisticDTO.StatisticResponseDTO.StatisticResponse;
import dadn_SmartFarm.model.DataPoint;
import dadn_SmartFarm.model.Statistic;
import dadn_SmartFarm.repository.StatisticRepository;
import dadn_SmartFarm.service.interf.IStatisticService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatisticService implements IStatisticService {
    private final StatisticRepository statisticRepository;
    private final DeviceService deviceService;

    @Override
    public StatisticResponse getStatistic(long feedId, LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.atTime(LocalTime.MAX); // 23:59:59.999999999

        List<Statistic> statistics = statisticRepository.findByFeedIdAndTimeStampBetween(feedId, from, to);

        if (statistics.isEmpty()) {
            return StatisticResponse.builder()
                    .code(404)
                    .message("No data found for " + date)
                    .authenticated(true)
                    .data(Collections.emptyList())
                    .max(0.0)
                    .min(0.0)
                    .average(0.0)
                    .threshold(false)
                    .build();
        }

        List<DataPoint> dataList = statistics.stream()
                .map(s -> new DataPoint(s.getValue(), s.getTimeStamp()))
                .toList();

        double THRESHOLD_VALUE = deviceService.getThresholdMaxByFeedId(feedId);

        double max = statistics.stream().mapToDouble(Statistic::getValue).max().orElse(0.0);
        double min = statistics.stream().mapToDouble(Statistic::getValue).min().orElse(0.0);
        double average = statistics.stream().mapToDouble(Statistic::getValue).average().orElse(0.0);
        boolean thresholdExceeded = statistics.stream().anyMatch(s -> s.getValue() > THRESHOLD_VALUE);

        return StatisticResponse.builder()
                .code(200)
                .message("Statistic for " + date + " fetched successfully")
                .authenticated(true)
                .data(dataList)
                .max(max)
                .min(min)
                .average(average)
                .threshold(thresholdExceeded)
                .build();
    }

}
