package gr.tourist_guides.ds.touristguidesapp.dto.activity;

import gr.tourist_guides.ds.touristguidesapp.core.enums.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record ActivityUpdateDTO(
        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        Long id,

        @NotEmpty(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        String uuid,

        @NotEmpty(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        @Size(max = 90, message = "Ο τίτλος μπορεί να περιέχει έως 90 χαρακτήρες.")
        String title,

        @NotNull
        @Size(max = 1000, message = "Η περιγραφή μπορεί να περιέχει έως 1000 χαρακτήρες.")
        String description,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        LocalDateTime dateTime,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        Double price,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        Status status,

        @NotNull(message = "Το πεδίο δεν μπορεί να είναι κενό.")
        Long categoryId
) {
}
