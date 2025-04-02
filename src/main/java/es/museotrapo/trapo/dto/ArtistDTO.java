package es.museotrapo.trapo.dto;

import es.museotrapo.trapo.model.Picture;

import java.util.List;

public record ArtistDTO (
        Long id,
        String name,
        String nickname,
        String birthDate,
        List<Picture> paintedPictures) {
}
   
