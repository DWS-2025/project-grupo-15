package es.museotrapo.trapo.exceptions;

public class UnauthorizedCommentDeleteException extends RuntimeException {
    public UnauthorizedCommentDeleteException(String message) {
        super(message);
    }
}

