package app.backend.dto.payload.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class JwtAuthResponse {
    private String token;
    private String tokenType = "Bearer";

    private String refreshToken;
    private String userName;
    private List<String> roles;


    public JwtAuthResponse(String token, String userName, String refreshToken , List<String> roles) {
        this.token = token;
        this.userName = userName;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {return refreshToken;}

    public void setRefreshToken() {this.refreshToken = refreshToken;}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getRoles() {
        return roles;
    }

}
