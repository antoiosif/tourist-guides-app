package gr.tourist_guides.ds.touristguidesapp.dto.visitor;

import gr.tourist_guides.ds.touristguidesapp.dto.user.UserUpdateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record VisitorUpdateDTO(
        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        Long id,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        Boolean isActive,

        @NotEmpty(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        String uuid,

        @Valid
        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        UserUpdateDTO userUpdateDTO,

        Long regionId
) {
}
