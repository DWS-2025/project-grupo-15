package es.museotrapo.trapo.controller.rest;

import es.museotrapo.trapo.exceptions.UnauthorizedCommentDeleteException;
import es.museotrapo.trapo.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandlerREST {

    // Manejar la UnauthorizedCommentDeleteException
    @ExceptionHandler(UnauthorizedCommentDeleteException.class)
    public ResponseEntity<?> handleUnauthorizedCommentDeleteException(UnauthorizedCommentDeleteException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage()));
    }

    // Manejar la excepción UserAlreadyExistsException
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict
                .body(Map.of("error", ex.getMessage()));
    }

}
