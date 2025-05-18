package es.museotrapo.trapo.controller.rest;

import es.museotrapo.trapo.exceptions.UnauthorizedCommentDeleteException;
import es.museotrapo.trapo.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**
 * Global exception handler for REST controllers.
 * Captures specific exceptions and provides appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandlerREST {

    /**
     * Handles the UnauthorizedCommentDeleteException.
     *
     * @param ex the exception indicating an unauthorized attempt to delete a comment.
     * @return a {@link ResponseEntity} with an HTTP 403 Forbidden status and an error message.
     */
    @ExceptionHandler(UnauthorizedCommentDeleteException.class)
    public ResponseEntity<?> handleUnauthorizedCommentDeleteException(UnauthorizedCommentDeleteException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN) // Return HTTP status 403 (Forbidden)
                .body(Map.of("error", ex.getMessage())); // Return the error message in the body
    }

    /**
     * Handles the UserAlreadyExistsException.
     *
     * @param ex the exception indicating that a user with the same credentials already exists.
     * @return a {@link ResponseEntity} with an HTTP 409 Conflict status and an error message.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT) // Return HTTP status 409 (Conflict)
                .body(Map.of("error", ex.getMessage())); // Return the error message in the body
    }
}
