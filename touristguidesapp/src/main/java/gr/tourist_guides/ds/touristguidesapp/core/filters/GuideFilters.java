package gr.tourist_guides.ds.touristguidesapp.core.filters;

import lombok.*;
import org.springframework.lang.Nullable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GuideFilters extends GenericFilters {
    @Nullable
    private Boolean isActive;

    @Nullable
    private String uuid;

    @Nullable
    private String recordNumber;

    @Nullable
    private String userUsername;

    @Nullable
    private String userFirstname;

    @Nullable
    private String userLastname;

    @Nullable
    private Long regionId;

    @Nullable
    private Long languageId;
}
