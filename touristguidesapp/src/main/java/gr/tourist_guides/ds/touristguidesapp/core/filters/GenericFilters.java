package gr.tourist_guides.ds.touristguidesapp.core.filters;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public abstract class GenericFilters {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String DEFAULT_SORT_COLUMN = "id";
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;
    private int page;
    private int pageSize;
    private String sortBy;
    private Sort.Direction sortDirection;

    public int getPage() {
        return Math.max(page, 0);
    }

    public int getPageSize() {
        return pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    public String getSortBy() {
        if (sortBy == null || sortBy.isBlank()) return DEFAULT_SORT_COLUMN;
        return sortBy;
    }

    public Sort.Direction getSortDirection() {
        if (sortDirection == null) return DEFAULT_SORT_DIRECTION;
        return sortDirection;
    }

    public Sort getSort() {
        return Sort.by(getSortDirection(), getSortBy());
    }

    public Pageable getPageable() {
        return PageRequest.of(getPage(), getPageSize(), getSort());
    }
}
