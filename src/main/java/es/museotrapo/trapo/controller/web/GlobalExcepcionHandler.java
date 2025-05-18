package es.museotrapo.trapo.controller.web;

import es.museotrapo.trapo.exceptions.UnauthorizedCommentDeleteException;
import es.museotrapo.trapo.exceptions.UserAlreadyExistsException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Global exception handler for the web application.
 * This handler intercepts specific exceptions and maps them to appropriate error views.
 * Using `@ControllerAdvice`, this class applies across all controllers.
 */
@ControllerAdvice
public class GlobalExcepcionHandler {

    /**
     * Handles `UnauthorizedCommentDeleteException` which occurs when a user tries to delete
     * a comment without proper authorization.
     *
     * @param ex The exception thrown when unauthorized access is attempted.
     * @return A `ModelAndView` object redirecting to the "error_forbidden" view, passing the error message.
     */
    @ExceptionHandler(UnauthorizedCommentDeleteException.class)
    public ModelAndView handleUnauthorizedCommentDeleteException(UnauthorizedCommentDeleteException ex) {
        // Create a ModelAndView object for displaying the error page
        ModelAndView modelAndView = new ModelAndView("error_forbidden"); // Redirect to "error_forbidden.html"
        modelAndView.addObject("errorMessage", ex.getMessage()); // Pass the exception's error message to the view
        return modelAndView;
    }

    /**
     * Handles `UserAlreadyExistsException` which occurs when trying to register
     * a user that already exists in the system.
     *
     * @param ex    The exception thrown if a duplicate user is detected.
     * @param model The UI model to which attributes can be added.
     * @return A `ModelAndView` object redirecting to the "error_forbidden" view, passing the error message.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView handleUserAlreadyExistsException(UserAlreadyExistsException ex, Model model) {
        // Create a ModelAndView object for displaying the error page
        ModelAndView modelAndView = new ModelAndView("error_forbidden"); // Redirect to the "error_forbidden.html"
        modelAndView.addObject("errorMessage", ex.getMessage()); // Pass the exception's error message to the view
        return modelAndView;
    }

}