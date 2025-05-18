package es.museotrapo.trapo.exceptions;

/**
 * Custom exception thrown when attempting to delete a comment
 * without having the required authorization.
 * This is a subclass of RuntimeException, meaning it is unchecked and does not need
 * to be declared in method signatures.
 */
public class UnauthorizedCommentDeleteException extends RuntimeException {

    /**
     * Constructs a new UnauthorizedCommentDeleteException with the specified detail message.
     *
     * @param message The detailed exception message explaining the cause of the error.
     */
    public UnauthorizedCommentDeleteException(String message) {
        super(message); // Pass the error message to the parent RuntimeException constructor
    }
}