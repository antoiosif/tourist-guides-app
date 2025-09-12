package gr.tourist_guides.ds.touristguidesapp.core.filters;

import gr.tourist_guides.ds.touristguidesapp.core.enums.Status;
import lombok.*;
import org.springframework.lang.Nullable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ActivityFilters extends GenericFilters {
    @Nullable
    private String uuid;

    @Nullable
    private String title;

    @Nullable
    private Status status;

    @Nullable
    private Long categoryId;
}
