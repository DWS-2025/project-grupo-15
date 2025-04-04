package es.museotrapo.trapo.dto;

import es.museotrapo.trapo.model.User;

public record CommentSimpleDTO(
        Long id,
        String message,
        String nameAuthor
) {
}
