package gr.tourist_guides.ds.touristguidesapp.dto.user;

import gr.tourist_guides.ds.touristguidesapp.core.enums.GenderType;
import gr.tourist_guides.ds.touristguidesapp.core.enums.Role;
import java.time.LocalDateTime;

public record UserReadOnlyDTO(
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long id,
        Boolean isActive,
        String username,
        String password,
        String firstname,
        String lastname,
        GenderType gender,
        Role role
) {
}
