package es.museotrapo.trapo.dto;

import org.mapstruct.Mapper;
import es.museotrapo.trapo.model.Artist;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    
    ArtistDTO toDTO(Artist artist);

    List<ArtistDTO> toDTOs(Collection<Artist> artists);

    Artist toDomain(ArtistDTO artistDTO);
}
