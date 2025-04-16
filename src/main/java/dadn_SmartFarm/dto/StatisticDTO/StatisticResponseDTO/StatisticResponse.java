package dadn_SmartFarm.dto.StatisticDTO.StatisticResponseDTO;

import dadn_SmartFarm.model.DataPoint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticResponse {
    int code;
    String message;
    boolean authenticated;
    List<DataPoint> data;
    double max;
    double min;
    double average;
    boolean threshold;
}
