package app.backend.Exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor

public class ErrorDetails {
    private final LocalDateTime timestamp;
    private final String message;
    private final HttpStatus httpStatus;
    private final String details;

}
