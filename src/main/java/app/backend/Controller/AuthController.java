package app.backend.Controller;

import app.backend.Service.AuthService;
import app.backend.dto.LoginDto;
import app.backend.dto.RegistrationDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(value = "*")
@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor


public class AuthController {

    private AuthService authService;

//    Build Register REST API
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationDto registrationDto){
        String response = authService.register(registrationDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

//    Build Login REST API
    @PostMapping("/login")
    public  ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        ResponseCookie response = authService.login(loginDto);
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item->item.getAuthority())
//                .collect(Collectors.toList());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,response.toString())
                .body("You have been signed-in.");
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signout() {
        ResponseCookie response = authService.signout();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,response.toString())
                .body("You have been signed-out.");
    }

}
