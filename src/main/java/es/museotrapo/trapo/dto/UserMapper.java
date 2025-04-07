package es.museotrapo.trapo.dto;

import org.mapstruct.Mapper;
import es.museotrapo.trapo.model.User;

import java.util.Collection;
import java.util.List;

// MapStruct mapper interface for converting between User domain objects and UserDTOs.
@Mapper(componentModel = "spring") // MapStruct annotation to generate implementation and register as a Spring bean
public interface UserMapper {

    /**
     * Converts a single User domain object into a UserDTO.
     *
     * @param user the User domain object to be converted
     * @return the corresponding UserDTO
     */
    UserDTO toDTO(User user);

    /**
     * Converts a collection of User domain objects into a list of UserDTOs.
     *
     * @param users a collection of User domain objects
     * @return a list of corresponding UserDTOs
     */
    List<UserDTO> toDTOs(Collection<User> users);

    /**
     * Converts a UserDTO into a User domain object.
     *
     * @param userDTO the UserDTO to be converted
     * @return the corresponding User domain object
     */
    User toDomain(UserDTO userDTO);
}
