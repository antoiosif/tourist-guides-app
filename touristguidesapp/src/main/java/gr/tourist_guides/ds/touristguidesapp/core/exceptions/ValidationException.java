package gr.tourist_guides.ds.touristguidesapp.core.exceptions;

import lombok.Getter;
import org.springframework.validation.BindingResult;
import java.io.Serial;

@Getter
public class ValidationException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;
    private final BindingResult bindingResult;

    public ValidationException(BindingResult bindingResult) {
        super("Validation failed");
        this.bindingResult = bindingResult;
    }
}
