package gr.tourist_guides.ds.touristguidesapp.dto.activity;

import gr.tourist_guides.ds.touristguidesapp.core.enums.Status;
import java.time.LocalDateTime;

public record ActivityReadOnlyDTO(
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long id,
        String uuid,
        String title,
        String description,
        LocalDateTime dateTime,
        Double price,
        Status status,
        String category     // category name
) {
}
