package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.Username;
import es.museotrapo.trapo.repository.UsernameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsernameService {

    @Autowired
    private UsernameRepository usernameRepository;

    /**
     * Returns the logged-in username for simplicity. This is a placeholder method.
     * In a real application, it should be replaced with actual authentication
     * logic.
     *
     * @return User - The logged-in username
     */
    public Username getLoggedUser() {
        // For now, return the first username in the repository as the logged-in username
        return usernameRepository.findAll().get(0);
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return List<User> - A list of all users
     */
    public List<Username> findAll() {
        return usernameRepository.findAll();
    }

    /**
     * Allows a username to like or remove a like on a picture.
     * If the picture is already liked, it removes the like. If not, it adds the
     * like.
     *
     * @param userId  The ID of the username performing the action (though unused here)
     * @param picture The picture to like or remove the like from
     */
    public void likeOrRemovePicture(Long userId, Picture picture) {

        Username username = getLoggedUser();// Get the logged-in username

        // Check if the username already likes the picture
        if (username.getLikedPictures().contains(picture)) {
            // If liked, remove the like
            username.getLikedPictures().remove(picture);
            picture.getUserLikes().remove(username);
        } else {
            // If not liked, add the like
            username.getLikedPictures().add(picture);
            picture.getUserLikes().add(username);
        }

        usernameRepository.save(username);// Save the updated username back to the repository
    }

    /**
     * Checks if a picture is liked by the currently logged-in username.
     *
     * @param picture The picture to check for a like
     * @return boolean - Returns true if the picture is liked, false otherwise
     */
    public boolean isPictureLiked(Picture picture) {

        Username username = getLoggedUser();// Get the logged-in username
        return username.getLikedPictures().contains(picture);// Return whether the picture is in the username's liked list
    }

}