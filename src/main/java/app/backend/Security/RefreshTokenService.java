package app.backend.Security;

import app.backend.Exception.TokenRefreshException;
import app.backend.Model.RefreshToken;
import app.backend.Repository.RefreshTokenRepository;
import app.backend.Repository.UserRepository;
import app.backend.Security.userService.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${app.jwt-RefreshSecret}")
    private String jwtRefreshSecret;

    @Value("${app.jwt-refreshExpiration-milliseconds}")
    private long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }
    public String generateJwtRefreshToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + refreshTokenDurationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public RefreshToken createRefreshToken(long userId){

        RefreshToken existingToken = refreshTokenRepository.findByUserId(userId).orElse(null);

        if(existingToken != null){
            existingToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            existingToken.setToken(UUID.randomUUID().toString());
            return refreshTokenRepository.save(existingToken);
        }
        else{
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUser(userRepository.findById(userId).get());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());

            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }
    }

    public RefreshToken validateExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token has been expired! Please Login again.");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }


}
