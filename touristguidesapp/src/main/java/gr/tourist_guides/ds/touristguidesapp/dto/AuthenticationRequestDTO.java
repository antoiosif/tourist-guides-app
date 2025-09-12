package gr.tourist_guides.ds.touristguidesapp.dto;

import jakarta.validation.constraints.NotEmpty;

public record AuthenticationRequestDTO(
        @NotEmpty
        String username,

        @NotEmpty
        String password
) {
}
