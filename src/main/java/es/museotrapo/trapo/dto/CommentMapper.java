package es.museotrapo.trapo.dto;

import org.mapstruct.Mapper;
import es.museotrapo.trapo.model.Comment;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDTO toDTO(Comment comment);

    List<CommentDTO> toDTOs(Collection<Comment> comments);

    Comment toDomain(CommentDTO commentDTO);
}
