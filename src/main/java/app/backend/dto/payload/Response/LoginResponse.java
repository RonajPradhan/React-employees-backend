package app.backend.dto.payload.Response;

import app.backend.Model.RefreshToken;
import app.backend.Security.userService.UserDetailsImpl;
import java.util.List;


public record LoginResponse(
        String token,
        String tokenType,
        String userName,
        String refreshToken,
        List<String> roles
) {
}
