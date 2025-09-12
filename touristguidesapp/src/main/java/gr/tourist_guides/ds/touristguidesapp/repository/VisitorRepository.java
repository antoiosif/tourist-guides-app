package gr.tourist_guides.ds.touristguidesapp.repository;

import gr.tourist_guides.ds.touristguidesapp.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long>, JpaSpecificationExecutor<Visitor> {
    Optional<Visitor> findByUuid(String uuid);
    Optional<Visitor> findByUserUsername(String username);
}
