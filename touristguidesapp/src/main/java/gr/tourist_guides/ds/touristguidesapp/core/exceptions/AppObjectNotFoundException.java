package gr.tourist_guides.ds.touristguidesapp.core.exceptions;

import java.io.Serial;

public class AppObjectNotFoundException extends AppGenericException {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_CODE = "NotFound";

    public AppObjectNotFoundException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
