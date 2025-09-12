package gr.tourist_guides.ds.touristguidesapp.core.exceptions;

import java.io.Serial;

public class AppObjectAlreadyExistsException extends AppGenericException {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_CODE = "AlreadyExists";

    public AppObjectAlreadyExistsException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
