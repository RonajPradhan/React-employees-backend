package app.backend.dto.payload.Request;

public record EmployeeRequest(
        long id,

        String firstName,

        String lastName,

        String emailId

) {
}
