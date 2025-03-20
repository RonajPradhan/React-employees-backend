package app.backend.dto.payload.Response;

import java.util.Set;

public record RegistrationResponse(
        String firstName,
        String lastName
) {
}
