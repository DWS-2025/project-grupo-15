package es.museotrapo.trapo.dto;

public record CommentSimpleDTO(
        Long id,
        String message,
        String nameAuthor
) {
}
