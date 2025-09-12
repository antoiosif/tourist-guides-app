package gr.tourist_guides.ds.touristguidesapp.core.specifications;

import gr.tourist_guides.ds.touristguidesapp.core.enums.Status;
import gr.tourist_guides.ds.touristguidesapp.model.Activity;
import gr.tourist_guides.ds.touristguidesapp.model.Category;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ActivitySpecification {

    /**
     * No instances of this class should be available.
     */
    private ActivitySpecification() {

    }

    public static Specification<Activity> activityStringFieldLike(String field, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.isBlank()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), "%" + value.toUpperCase() + "%");   // case-insensitive search
        };
    }

    public static Specification<Activity> activityStatusIs(Status status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Activity> activityCategoryIdIs(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Activity, Category> category = root.join("category");
            return criteriaBuilder.equal(category.get("id"), id);
        };
    }
}
