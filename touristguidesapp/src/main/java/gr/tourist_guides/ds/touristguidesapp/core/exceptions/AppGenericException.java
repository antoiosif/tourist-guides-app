package gr.tourist_guides.ds.touristguidesapp.core.exceptions;

import lombok.Getter;
import java.io.Serial;

@Getter
public class AppGenericException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String code;

    public AppGenericException(String code, String message) {
        super(message);
        this.code = code;
    }
}
