package es.museotrapo.trapo.service;

import es.museotrapo.trapo.dto.PictureDTO;
import es.museotrapo.trapo.dto.UserDTO;
import es.museotrapo.trapo.dto.UserMapper;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.PictureRepository;
import es.museotrapo.trapo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private UserMapper mapper;

    /**
     * Returns the logged-in username for simplicity. This is a placeholder method.
     * In a real application, it should be replaced with actual authentication
     * logic.
     *
     * @return User - The logged-in username
     */
    public UserDTO getLoggedUser() {
        // For now, return the first username in the repository as the logged-in username
         return toDTO(userRepository.findAll().get(0));
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return List<User> - A list of all users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void likeOrRemovePicture(PictureDTO pictureDTO) {

        Picture picture = pictureRepository.findById(pictureDTO.id()).get();
        User user = toDomain(this.getLoggedUser());// Get the logged-in username

        // Check if the username already likes the picture
        if (user.getLikedPictures().contains(picture)) {
            // If liked, remove the like
            user.getLikedPictures().remove(picture);
            picture.getUserLikes().remove(user);
        } else {
            // If not liked, add the like
            user.getLikedPictures().add(picture);
            picture.getUserLikes().add(user);
        }
        userRepository.save(user);// Save the updated username back to the repository
    }

    public boolean isPictureLiked(PictureDTO pictureDTO) {
        User user = toDomain(getLoggedUser());// Get the logged-in username
        return user.getLikedPictures().contains(pictureRepository.findById(pictureDTO.id()).get());// Return whether the picture is in the username's liked list
    }

    private UserDTO toDTO(User user) {
        return mapper.toDTO(user);
    }

    protected User toDomain(UserDTO userDTO){
        return mapper.toDomain(userDTO);
    }

    private Collection<UserDTO> toDTOs(Collection<User> users){
        return mapper.toDTOs(users);
    }
}