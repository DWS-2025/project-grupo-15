package es.museotrapo.trapo.dto;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.museotrapo.trapo.model.Picture;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface PictureMapper {

    PictureDTO toDTO(Picture picture);

    Collection<PictureDTO> toDTOs(Collection<Picture> pictures);

    @Mapping(target = "imageFile", ignore = true)
    Picture toDomain(PictureDTO pictureDTO);
}
