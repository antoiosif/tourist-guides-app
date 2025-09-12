package gr.tourist_guides.ds.touristguidesapp.dto.visitor;

import gr.tourist_guides.ds.touristguidesapp.dto.user.UserInsertDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record VisitorInsertDTO(
        @Valid
        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        UserInsertDTO userInsertDTO,

        Long regionId
) {
}
