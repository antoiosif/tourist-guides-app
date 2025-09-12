package gr.tourist_guides.ds.touristguidesapp.dto.user;

import gr.tourist_guides.ds.touristguidesapp.core.enums.GenderType;
import gr.tourist_guides.ds.touristguidesapp.core.enums.Role;
import jakarta.validation.constraints.*;

public record UserUpdateDTO (
        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        Long id,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        Boolean isActive,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        @Email(message = "Μη έγκυρο email.")
        String username,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&+=])^\\S{8,}$",
                message = "Το password πρέπει να περιέχει τουλάχιστον 8 χαρακτήρες εκ των οποίων τουλάχιστον \\\n" +
                        "  1 πεζό, 1 κεφαλαίο, 1 ψηφίο και 1 ειδικό χαρακτήρα (!@#$%^&+=) χωρίς κενά.")
        String password,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        @Pattern(regexp = "^\\S{2,}$", message = "Το όνομα πρέπει να περιέχει τουλάχιστον 2 χαρακτήρες και καθόλου κενά.")
        String firstname,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        @Pattern(regexp = "^\\S{2,}$", message = "Το επώνυμο πρέπει να περιέχει τουλάχιστον 2 χαρακτήρες και καθόλου κενά.")
        String lastname,

        GenderType gender,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        Role role
) {
}
