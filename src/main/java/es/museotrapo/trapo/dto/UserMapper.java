package es.museotrapo.trapo.dto;

import org.mapstruct.Mapper;
import es.museotrapo.trapo.model.User;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    List<UserDTO> toDTOs(Collection<User> users);

    User toDomain(UserDTO userDTO);
}
