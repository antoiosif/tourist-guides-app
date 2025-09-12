package gr.tourist_guides.ds.touristguidesapp.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryInsertDTO(
        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        @Size(min = 2, message = "Το όνομα πρέπει να περιέχει τουλάχιστον 2 χαρακτήρες.")
        String name
) {
}
