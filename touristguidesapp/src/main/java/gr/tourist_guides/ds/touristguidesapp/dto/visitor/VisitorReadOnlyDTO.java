package gr.tourist_guides.ds.touristguidesapp.dto.visitor;

import gr.tourist_guides.ds.touristguidesapp.dto.user.UserReadOnlyDTO;
import java.time.LocalDateTime;

public record VisitorReadOnlyDTO(
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long id,
        Boolean isActive,
        String uuid,
        UserReadOnlyDTO userReadOnlyDTO,
        String region   // region name
) {
}
