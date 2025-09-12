package gr.tourist_guides.ds.touristguidesapp.dto;

public record ResponseMessageDTO(
        String code,
        String description
) {
    public ResponseMessageDTO(String code) {
        this(code, "");
    }
}
