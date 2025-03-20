package app.backend.dto.payload.Request;

import java.util.Set;

public record RegistrationRequest(
        String firstName,
        String lastName,
        String userName,
        String password,
        String email,
        Set<String> role
) {
}
