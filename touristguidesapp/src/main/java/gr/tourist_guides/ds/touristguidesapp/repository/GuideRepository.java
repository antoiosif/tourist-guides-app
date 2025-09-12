package gr.tourist_guides.ds.touristguidesapp.repository;

import gr.tourist_guides.ds.touristguidesapp.model.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long>, JpaSpecificationExecutor<Guide> {
    Optional<Guide> findByUuid(String uuid);
    Optional<Guide> findByRecordNumber(String recordNumber);
    Optional<Guide> findByUserUsername(String username);
}
