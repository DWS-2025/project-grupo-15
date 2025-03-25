package es.museotrapo.trapo.dto;

import es.museotrapo.trapo.model.Artist;

public record PictureDTO(
    Long id,
    String name,
    String date,
    String image,
    Artist artist){
}
