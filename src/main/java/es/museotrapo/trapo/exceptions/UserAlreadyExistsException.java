package es.museotrapo.trapo.exceptions;

/**
 * Custom exception thrown when attempting to add a new user
 * who already exists in the system.
 * This exception extends RuntimeException, making it unchecked and
 * allowing it to be thrown during runtime without needing to be declared.
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new UserAlreadyExistsException with the specified detail message.
     *
     * @param message The detail message explaining the cause of the exception.
     */
    public UserAlreadyExistsException(String message) {
        super(message); // Pass the exception message to the parent RuntimeException constructor
    }
}