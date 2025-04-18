package dadn_SmartFarm.repository;

import dadn_SmartFarm.model.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    List<Statistic> findByFeedIdAndTimeStampBetween(long feedId, LocalDateTime from, LocalDateTime to);
}
