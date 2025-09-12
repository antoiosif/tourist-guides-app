package gr.tourist_guides.ds.touristguidesapp.core.filters;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
@Setter
public class Paginated<T> {
    List<T> data;
    long totalElements;
    int totalPages;
    int numberOfElements;
    int currentPage;
    int pageSize;

    public Paginated(Page<T> page) {
        data = page.getContent();
        totalElements = page.getTotalElements();
        totalPages = page.getTotalPages();
        numberOfElements = page.getNumberOfElements();
        currentPage = page.getNumber();
        pageSize = page.getSize();
    }
}
