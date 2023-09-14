package app.backend.Service;

import app.backend.dto.LoginDto;
import app.backend.dto.RegistrationDto;

public interface AuthService {
    String register(RegistrationDto registrationDto);

    String login(LoginDto loginDto);
}
