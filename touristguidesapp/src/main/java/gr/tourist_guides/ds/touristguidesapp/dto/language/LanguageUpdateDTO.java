package gr.tourist_guides.ds.touristguidesapp.dto.language;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LanguageUpdateDTO(
        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        Long id,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        @Pattern(regexp = "^[a-zA-Z]{3}$", message = "Ο κωδικός πρέπει να αποτελείται από 3 λατινικούς χαρακτήρες.")
        String code,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        @Size(min = 2, message = "Το όνομα πρέπει να περιέχει τουλάχιστον 2 χαρακτήρες.")
        String name
) {
}
