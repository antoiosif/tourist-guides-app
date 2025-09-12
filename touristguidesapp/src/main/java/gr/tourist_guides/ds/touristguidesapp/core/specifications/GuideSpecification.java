package gr.tourist_guides.ds.touristguidesapp.core.specifications;

import gr.tourist_guides.ds.touristguidesapp.model.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class GuideSpecification {

    /**
     * No instances of this class should be available.
     */
    private GuideSpecification() {

    }

    public static Specification<Guide> guideIsActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<Guide> guideStringFieldLike(String field, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.isBlank()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), "%" + value.toUpperCase() + "%");   // case-insensitive search
        };
    }

    public static Specification<Guide> guideUserStringFieldLike(String field, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.isBlank()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Guide, User> user = root.join("user");
            return criteriaBuilder.like(criteriaBuilder.upper(user.get(field)), "%" + value.toUpperCase() + "%");   // case-insensitive search
        };
    }

    public static Specification<Guide> guideRegionIdIs(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Guide, Region> region = root.join("region");
            return criteriaBuilder.equal(region.get("id"), id);
        };
    }

    public static Specification<Guide> guideLanguageIdIs(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Guide, Language> language = root.join("language");
            return criteriaBuilder.equal(language.get("id"), id);
        };
    }
}
