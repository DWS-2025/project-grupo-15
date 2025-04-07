package es.museotrapo.trapo.dto;

import org.mapstruct.Mapper;
import es.museotrapo.trapo.model.Comment;

import java.util.Collection;
import java.util.List;

/**
 * This interface defines a mapper for converting between Comment (domain model) 
 * and CommentDTO (data transfer object). 
 * It's used to separate internal data representation from what is exposed externally.
 */
@Mapper(componentModel = "spring") // MapStruct annotation to generate implementation and register as a Spring bean
public interface CommentMapper {

    /**
     * Converts a single Comment domain object into a CommentDTO.
     *
     * @param comment the domain object to be converted
     * @return the corresponding CommentDTO
     */
    CommentDTO toDTO(Comment comment);

    /**
     * Converts a collection of Comment domain objects into a list of CommentDTOs.
     *
     * @param comments a collection of Comment objects
     * @return a list of corresponding CommentDTOs
     */
    List<CommentDTO> toDTOs(Collection<Comment> comments);

    /**
     * Converts a CommentDTO into a Comment domain object.
     *
     * @param commentDTO the data transfer object to be converted
     * @return the corresponding Comment domain object
     */
    Comment toDomain(CommentDTO commentDTO);
}

