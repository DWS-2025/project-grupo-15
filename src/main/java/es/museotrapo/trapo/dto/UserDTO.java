package es.museotrapo.trapo.dto;

import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;

import java.util.List;

public record UserDTO (
        Long id,
        String name,
        String email,
        List<PictureSimpleDTO> likedPictures,
        List<CommentSimpleDTO> comments) {
}
