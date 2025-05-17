package es.museotrapo.trapo.dto;

import java.util.List;

public record UserDTO(
        Long id,
        String name,
        String email,
        String encodedPassword,
        List<PictureSimpleDTO> likedPictures,
        List<CommentSimpleDTO> comments) {
}
