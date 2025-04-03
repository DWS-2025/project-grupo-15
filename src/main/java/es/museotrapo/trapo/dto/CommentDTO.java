package es.museotrapo.trapo.dto;

public record CommentDTO (
        Long id,
        String message,
        String nameAuthor) {
}
