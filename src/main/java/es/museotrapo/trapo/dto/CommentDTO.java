package es.museotrapo.trapo.dto;

import es.museotrapo.trapo.model.User;

public record CommentDTO(
        Long id,
        String message,
        UserSimpleDTO author) {
}
