package gr.tourist_guides.ds.touristguidesapp.repository;

import gr.tourist_guides.ds.touristguidesapp.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByCode(String code);
    Optional<Language> findByName(String name);
}
