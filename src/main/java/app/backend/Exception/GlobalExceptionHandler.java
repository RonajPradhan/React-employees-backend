package app.backend.Exception;


import java.time.LocalDateTime;
import java.util.Date;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends Throwable {

    @ExceptionHandler(ToDoAPIException.class)
    public ResponseEntity<ErrorDetails> handleToDoAPIException(ToDoAPIException exception,WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                exception.getStatus(),
                webRequest.getDescription(false)
        );

        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                HttpStatus.NOT_FOUND,
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ErrorDetails> handleTokenRefreshException(TokenRefreshException exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                HttpStatus.FORBIDDEN,
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails,HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception ex){
        ProblemDetail errorDetail = null;

        if(ex instanceof BadCredentialsException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401),ex.getMessage());
            errorDetail.setProperty("access_denied_reason","Authentication Failure");

        }

        if(ex instanceof AccessDeniedException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),ex.getMessage());
            errorDetail.setProperty("access_denied_reason","Not Authorized");
        }

        if(ex instanceof ExpiredJwtException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),ex.getMessage());
            errorDetail.setProperty("access_denied_reason","JWT Token already expired");
        }

        if(ex instanceof SignatureException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),ex.getMessage());
            errorDetail.setProperty("access_denied_reason","JWT signature not valid");
        }

        return errorDetail;
    }

}
