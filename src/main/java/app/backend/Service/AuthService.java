package app.backend.Service;

import app.backend.dto.LoginDto;
import app.backend.dto.RegistrationDto;
import org.springframework.http.ResponseCookie;

public interface AuthService {
    String register(RegistrationDto registrationDto);

    ResponseCookie login(LoginDto loginDto);

    ResponseCookie signout();
}
