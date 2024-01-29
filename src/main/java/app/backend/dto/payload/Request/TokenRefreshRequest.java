package app.backend.dto.payload.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshRequest {

    private String refreshToken;


}
