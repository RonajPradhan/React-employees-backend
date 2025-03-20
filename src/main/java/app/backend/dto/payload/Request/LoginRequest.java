package app.backend.dto.payload.Request;

public record LoginRequest(
        String userNameOrEmail,
        String password
) {
}
