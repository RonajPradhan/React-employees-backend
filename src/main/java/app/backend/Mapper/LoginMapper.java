package app.backend.Mapper;

import app.backend.Model.RefreshToken;
import app.backend.Model.User;
import app.backend.dto.payload.Request.LoginRequest;
import app.backend.dto.payload.Response.JwtAuthResponse;
import app.backend.dto.payload.Response.LoginResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginMapper {

    public User toLogin(LoginRequest loginRequest) {
        return User.builder()
                .userName(loginRequest.userNameOrEmail())
                .password(loginRequest.password())
                .build();
    }

    public LoginResponse fromLogin(String jwtToken, String userName, RefreshToken refreshToken, List<String> roles) {
        return new LoginResponse(
                jwtToken,
                "Bearer",
                userName,
                refreshToken.getToken(),
                roles
        );
    }

}
