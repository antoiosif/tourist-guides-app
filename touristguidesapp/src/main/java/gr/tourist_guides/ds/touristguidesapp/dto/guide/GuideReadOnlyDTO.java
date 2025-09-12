package gr.tourist_guides.ds.touristguidesapp.dto.guide;

import gr.tourist_guides.ds.touristguidesapp.dto.user.UserReadOnlyDTO;
import java.time.LocalDateTime;
import java.util.Date;

public record GuideReadOnlyDTO(
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long id,
        Boolean isActive,
        String uuid,
        String recordNumber,
        Date dateOfIssue,
        String phoneNumber,
        String email,
        String bio,
        UserReadOnlyDTO userReadOnlyDTO,
        String region,      // region name
        String language     // language name
) {
}
