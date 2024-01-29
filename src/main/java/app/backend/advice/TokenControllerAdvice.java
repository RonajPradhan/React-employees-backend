package app.backend.advice;

import app.backend.Exception.ResourceNotFoundException;
import app.backend.Exception.TokenRefreshException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

    @RestControllerAdvice
    public class TokenControllerAdvice {
        @ExceptionHandler(value = app.backend.Exception.TokenRefreshException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN)
        public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
            return new ErrorMessage(
                    HttpStatus.FORBIDDEN.value(),
                    new Date(),
                    ex.getMessage(),
                    request.getDescription(false));
        }

        @ExceptionHandler(value = app.backend.Exception.ResourceNotFoundException.class)
        @ResponseStatus(value = HttpStatus.NOT_FOUND)
        public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
            return new ErrorMessage(
                    HttpStatus.NOT_FOUND.value(),
                    new Date(),
                    ex.getMessage(),
                    request.getDescription(false)
            );


        }

        @ExceptionHandler(value = Exception.class)
        @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
        public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
            return new ErrorMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    new Date(),
                    ex.getMessage(),
                    request.getDescription(false)
            );
        }
    }

