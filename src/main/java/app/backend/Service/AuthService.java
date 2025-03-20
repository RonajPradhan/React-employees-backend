package app.backend.Service;

import app.backend.dto.payload.Request.LoginRequest;
import app.backend.dto.payload.Request.RegistrationRequest;
import app.backend.dto.payload.Request.TokenRefreshRequest;
import app.backend.dto.payload.Response.RegistrationResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    RegistrationResponse register(RegistrationRequest registrationRequest);

    ResponseEntity<?> login(LoginRequest loginRequest);

    ResponseEntity<?> refreshToken(TokenRefreshRequest tokenRefreshRequest);

    ResponseEntity<?> logout();

}
