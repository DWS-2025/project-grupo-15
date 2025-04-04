package es.museotrapo.trapo.dto;

import java.util.List;

public record UserSimpleDTO(
        Long id,
        String name,
        String email,
        List<String> nameLikedPictures,
        List<String> commentsMessage
) {
}
