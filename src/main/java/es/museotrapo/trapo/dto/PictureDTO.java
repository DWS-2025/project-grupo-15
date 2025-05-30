package es.museotrapo.trapo.dto;

import java.util.List;

public record PictureDTO(
        Long id,
        String name,
        String date,
        String image,
        Long artistId,
        ArtistSimpleDTO artist,
        List<UserSimpleDTO> userLikes,
        List<CommentSimpleDTO> comments) {
}
