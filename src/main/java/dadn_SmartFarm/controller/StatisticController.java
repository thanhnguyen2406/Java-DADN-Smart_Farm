package dadn_SmartFarm.controller;

import dadn_SmartFarm.dto.StatisticDTO.StatisticResponseDTO.StatisticResponse;
import dadn_SmartFarm.service.implement.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping
    public StatisticResponse getStatistic(@RequestParam long feedId,
                                          @RequestParam LocalDate date) {
        return statisticService.getStatistic(feedId, date);
    }
}
