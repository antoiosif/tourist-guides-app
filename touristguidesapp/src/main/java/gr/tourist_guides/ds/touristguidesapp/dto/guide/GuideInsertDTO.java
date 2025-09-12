package gr.tourist_guides.ds.touristguidesapp.dto.guide;

import gr.tourist_guides.ds.touristguidesapp.dto.user.UserInsertDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Date;

public record GuideInsertDTO(
        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        @Pattern(regexp = "^\\S{5,}$", message = "Ο Αριθμός Μητρώου πρέπει να περιέχει τουλάχιστον 5 χαρακτήρες και καθόλου κενά.")
        String recordNumber,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        Date dateOfIssue,

        @Pattern(regexp = "^\\d{10}$", message = "Ο τηλεφωνικός αριθμός πρέπει να αποτελείται από 10 ψηφία χωρίς κενά.")
        String phoneNumber,

        @Email(message = "Μη έγκυρο email.")
        String email,

        @Size(max = 5000, message = "Το βιογραφικό μπορεί να περιέχει έως 5000 χαρακτήρες.")
        String bio,

        @Valid
        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        UserInsertDTO userInsertDTO,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        Long regionId,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        Long languageId
) {
}
