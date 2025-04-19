package dadn_SmartFarm.repository;

import dadn_SmartFarm.model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    public Page<Log> findByFeedKey(String feedKey, Pageable pageable);
}
