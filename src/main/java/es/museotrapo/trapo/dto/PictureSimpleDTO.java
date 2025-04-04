package es.museotrapo.trapo.dto;

import java.util.List;

public record PictureSimpleDTO (
    Long id,
    String name,
    String date,
    String image,
    String artistNickname,
    List<String> nameUserLikes,
    List<String> commentsMessage) {
}
