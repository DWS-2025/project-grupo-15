package es.museotrapo.trapo.dto;

import java.util.List;

public record PictureDTO(
    Long id,
    String name,
    String date,
    String image,
    String artistNickname,
    Long artistId,
    List<String> nameUserLikes,
    List<CommentDTO> comments){
}
