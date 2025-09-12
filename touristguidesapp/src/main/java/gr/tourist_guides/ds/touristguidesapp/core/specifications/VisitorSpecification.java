package gr.tourist_guides.ds.touristguidesapp.core.specifications;

import gr.tourist_guides.ds.touristguidesapp.model.Region;
import gr.tourist_guides.ds.touristguidesapp.model.User;
import gr.tourist_guides.ds.touristguidesapp.model.Visitor;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class VisitorSpecification {

    /**
     * No instances of this class should be available.
     */
    private VisitorSpecification() {

    }

    public static Specification<Visitor> visitorIsActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<Visitor> visitorStringFieldLike(String field, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.isBlank()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), "%" + value.toUpperCase() + "%");   // case-insensitive search
        };
    }

    public static Specification<Visitor> visitorUserStringFieldLike(String field, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.isBlank()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Visitor, User> user = root.join("user");
            return criteriaBuilder.like(criteriaBuilder.upper(user.get(field)), "%" + value.toUpperCase() + "%");   // case-insensitive search
        };
    }

    public static Specification<Visitor> visitorRegionIdIs(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Visitor, Region> region = root.join("region");
            return criteriaBuilder.equal(region.get("id"), id);
        };
    }
}
