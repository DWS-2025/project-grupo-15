package es.museotrapo.trapo.controller.rest;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global exception handler specifically for handling cases where a requested element
 * is not found (e.g., missing database entries or invalid lookups).
 */
@ControllerAdvice
public class NoSuchElementExceptionCA {

    /**
     * Handles the NoSuchElementException globally.
     *
     * This exception is thrown when a requested element does not exist (e.g., searching
     * for a user or item that is not present in the system).
     *
     * @ResponseStatus ensures that clients receive a 404 NOT FOUND HTTP status code
     * whenever this exception is encountered.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public void handleNotFound() {
        // No body content is returned. Just the 404 status is sent to the client.
    }
}