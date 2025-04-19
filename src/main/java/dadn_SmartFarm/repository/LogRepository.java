package dadn_SmartFarm.repository;

import dadn_SmartFarm.model.Log;
import dadn_SmartFarm.model.enums.LogType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    Page<Log> findByFeedKey(String feedKey, Pageable pageable);
    Page<Log> findByFeedKeyAndLogType(String feedKey, LogType logType, Pageable pageable);
}
