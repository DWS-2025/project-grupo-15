package es.museotrapo.trapo.dto;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Comment;

import java.util.List;

public record PictureDTO(
    Long id,
    String name,
    String date,
    String image,
    Artist artist,
    long numLikes,
    List<Comment> comments){
}
