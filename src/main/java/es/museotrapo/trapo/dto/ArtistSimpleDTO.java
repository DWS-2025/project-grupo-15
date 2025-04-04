package es.museotrapo.trapo.dto;

import java.util.List;

public record ArtistSimpleDTO(
        Long id,
        String name,
        String nickname,
        String birthDate,
        List<String> namePaintedPictures
) {
}
