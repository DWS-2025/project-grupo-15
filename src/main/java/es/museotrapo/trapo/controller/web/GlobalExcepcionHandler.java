package es.museotrapo.trapo.controller.web;

import es.museotrapo.trapo.exceptions.UnauthorizedCommentDeleteException;
import es.museotrapo.trapo.exceptions.UserAlreadyExistsException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExcepcionHandler {

    // Manejar UnauthorizedCommentDeleteException
    @ExceptionHandler(UnauthorizedCommentDeleteException.class)
    public ModelAndView handleUnauthorizedCommentDeleteException(UnauthorizedCommentDeleteException ex) {
        ModelAndView modelAndView = new ModelAndView("error_forbidden"); // Redirigir a forbidden.html
        modelAndView.addObject("errorMessage", ex.getMessage()); // Pasar el mensaje
        return modelAndView;
    }

    // Manejar UserAlreadyExistsException
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView handleUserAlreadyExistsException(UserAlreadyExistsException ex, Model model) {
        ModelAndView modelAndView = new ModelAndView("error_forbidden"); // Redirigir a conflict.html
        modelAndView.addObject("errorMessage", ex.getMessage()); // Pasar el mensaje
        return modelAndView;
    }

}