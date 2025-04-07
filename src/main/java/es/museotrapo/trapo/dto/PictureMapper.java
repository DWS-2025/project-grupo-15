package es.museotrapo.trapo.dto;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.museotrapo.trapo.model.Picture;

import java.util.Collection;


// MapStruct mapper interface for converting between Picture domain objects and PictureDTOs.
@Mapper(componentModel = "spring") // MapStruct annotation to generate implementation and register as a Spring bean
public interface PictureMapper {

    /**
     * Converts a single Picture domain object into a PictureDTO.
     *
     * @param picture the Picture domain object to be converted
     * @return the corresponding PictureDTO
     */
    PictureDTO toDTO(Picture picture);

    /**
     * Converts a collection of Picture domain objects into a collection of PictureDTOs.
     *
     * @param pictures a collection of Picture domain objects
     * @return a collection of corresponding PictureDTOs
     */
    Collection<PictureDTO> toDTOs(Collection<Picture> pictures);

    /**
     * Converts a PictureDTO into a Picture domain object.
     * This method ignores the 'imageFile' field during the conversion.
     * The 'imageFile' field might be handled differently or excluded intentionally.
     *
     * @param pictureDTO the PictureDTO to be converted
     * @return the corresponding Picture domain object
     */
    @Mapping(target = "imageFile", ignore = true)
    // Ignore the 'imageFile' field during mapping
    Picture toDomain(PictureDTO pictureDTO);
}
