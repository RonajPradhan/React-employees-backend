package app.backend.Service.impl;

import app.backend.Exception.ToDoAPIException;
import app.backend.Exception.TokenRefreshException;
import app.backend.Mapper.LoginMapper;
import app.backend.Mapper.RegistrationMapper;
import app.backend.Model.RefreshToken;
import app.backend.Model.User;
import app.backend.Repository.UserRepository;
import app.backend.Security.JwtTokenProvider;
import app.backend.Security.RefreshTokenService;
import app.backend.Security.userService.UserDetailsImpl;
import app.backend.Service.AuthService;
import app.backend.dto.payload.Request.LoginRequest;
import app.backend.dto.payload.Request.RegistrationRequest;
import app.backend.dto.payload.Request.TokenRefreshRequest;
import app.backend.dto.payload.Response.LoginResponse;
import app.backend.dto.payload.Response.RegistrationResponse;
import app.backend.dto.payload.Response.TokenRefreshResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private RegistrationMapper registrationMapper;
    private LoginMapper loginMapper;
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;

    private RefreshTokenService refreshTokenService;
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public RegistrationResponse register(RegistrationRequest registrationRequest) {

        if(userRepository.existsByUserName(registrationRequest.userName())){
            throw new ToDoAPIException(HttpStatus.BAD_REQUEST,"Username already exists!");
        }

        if(userRepository.existsByEmail(registrationRequest.email())){
            throw new ToDoAPIException(HttpStatus.BAD_REQUEST,"Email already exists");
        }

        User savedUser = userRepository.save(registrationMapper.toRegister(registrationRequest));

        return registrationMapper.fromRegister(savedUser);

    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest){
    System.out.print(loginRequest);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.userNameOrEmail(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtTokenProvider.generateJwtToken(authentication);

        List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

        Long userId = userDetails.getId();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userId);

        LoginResponse response = loginMapper.fromLogin(
                jwt,
                userDetails.getUsername(),
                refreshToken,
                roles
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> refreshToken(TokenRefreshRequest request){
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::validateExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenProvider.generateTokenFromUsername(user.getUserName());
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
                    return ResponseEntity.ok(new TokenRefreshResponse(token,refreshToken.getToken()));
        }).orElseThrow(() -> new TokenRefreshException(requestRefreshToken,"Refresh Token is not in database."));
    }

    @Override
    public ResponseEntity<?> logout(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok("Logout successful");
    }
}
