package dadn_SmartFarm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedInfo {
    private Long feedId;
    private Double threshold_max;
    private Double threshold_min;
}
