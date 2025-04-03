package es.museotrapo.trapo.dto;


import java.util.List;

public record ArtistDTO (
        Long id,
        String name,
        String nickname,
        String birthDate,
        List<PictureDTO> paintedPictures) {
}
   
