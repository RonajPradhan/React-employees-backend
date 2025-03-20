package app.backend.dto.payload.Response;

import java.time.LocalDateTime;

public record EmployeeResponse(
        long id,
        String firstName,
        String lastName,
        String emailId,
        LocalDateTime createdAt
) {
}
