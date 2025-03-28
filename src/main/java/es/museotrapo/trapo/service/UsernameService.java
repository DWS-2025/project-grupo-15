package es.museotrapo.trapo.service;

import es.museotrapo.trapo.dto.PictureDTO;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.PictureRepository;
import es.museotrapo.trapo.repository.UsernameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsernameService {

    @Autowired
    private UsernameRepository usernameRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private PictureService pictureService;

    /**
     * Returns the logged-in username for simplicity. This is a placeholder method.
     * In a real application, it should be replaced with actual authentication
     * logic.
     *
     * @return User - The logged-in username
     */
    public User getLoggedUser() {
        // For now, return the first username in the repository as the logged-in username
        return usernameRepository.findAll().get(0);
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return List<User> - A list of all users
     */
    public List<User> findAll() {
        return usernameRepository.findAll();
    }

    public void likeOrRemovePicture(PictureDTO pictureDTO) {

        Picture picture = pictureService.toDomain(pictureDTO);
        User user = getLoggedUser();// Get the logged-in username

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

        usernameRepository.save(user);// Save the updated username back to the repository
    }

    /**
     * Checks if a picture is liked by the currently logged-in username.
     *
     * @param picture The picture to check for a like
     * @return boolean - Returns true if the picture is liked, false otherwise
     */
    public boolean isPictureLiked(PictureDTO pictureDTO) {


        User user = getLoggedUser();// Get the logged-in username
        return user.getLikedPictures().contains(picture);// Return whether the picture is in the username's liked list
    }

}