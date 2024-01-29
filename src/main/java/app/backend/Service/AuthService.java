package app.backend.Service;

import app.backend.Security.userService.UserDetailsImpl;
import app.backend.dto.LoginDto;
import app.backend.dto.RegistrationDto;
import app.backend.dto.payload.Request.TokenRefreshRequest;
import app.backend.dto.payload.Response.TokenRefreshResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    String register(RegistrationDto registrationDto);

    ResponseEntity<?> login(LoginDto loginDto);

    ResponseEntity<?> refreshToken(TokenRefreshRequest tokenRefreshRequest);

    ResponseEntity<?> logout();

}
