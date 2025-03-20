package app.backend.Controller;

import app.backend.Service.AuthService;
import app.backend.dto.payload.Request.LoginRequest;
import app.backend.dto.payload.Request.RegistrationRequest;
import app.backend.dto.payload.Request.TokenRefreshRequest;
import app.backend.dto.payload.Response.RegistrationResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(value = "*")
@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor


public class AuthController {

    private AuthService authService;

//    Build Register REST API
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest registrationRequest){
        RegistrationResponse response = authService.register(registrationRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

//    Build Login REST API
    @PostMapping("/login")
    public  ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
       return authService.login(loginRequest);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest){
        return authService.refreshToken(tokenRefreshRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        return authService.logout();
    }


}
