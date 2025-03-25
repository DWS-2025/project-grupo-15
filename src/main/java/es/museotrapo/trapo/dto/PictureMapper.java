package es.museotrapo.trapo.dto;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.museotrapo.trapo.model.Picture;

import java.util.Collection;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface PictureMapper {

    PictureDTO toPictureDTO(Optional<Picture> picture);

    Collection<PictureDTO> toPictureDTOs(Collection<Picture> pictures);

    @Mapping(target = "imageFile", ignore = true)
    Picture toDomain(PictureDTO pictureDTO);
}
