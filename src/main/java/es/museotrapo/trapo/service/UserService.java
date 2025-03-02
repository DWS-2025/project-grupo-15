package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Returns the logged-in user for simplicity. This is a placeholder method.
     * In a real application, it should be replaced with actual authentication
     * logic.
     *
     * @return User - The logged-in user
     */
    public User getLoggedUser() {
        // For now, return the first user in the repository as the logged-in user
        return userRepository.findAll().get(0);
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return List<User> - A list of all users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Allows a user to like or remove a like on a picture.
     * If the picture is already liked, it removes the like. If not, it adds the
     * like.
     *
     * @param userId  The ID of the user performing the action (though unused here)
     * @param picture The picture to like or remove the like from
     */
    public void likeOrRemovePicture(Long userId, Picture picture) {

        User user = getLoggedUser();// Get the logged-in user

        // Check if the user already likes the picture
        if (user.getLikedPictures().contains(picture)) {
            // If liked, remove the like
            user.getLikedPictures().remove(picture);
            picture.getUserLikes().remove(user);
        } else {
            // If not liked, add the like
            user.getLikedPictures().add(picture);
            picture.getUserLikes().add(user);
        }

        userRepository.save(user);// Save the updated user back to the repository
    }

    /**
     * Checks if a picture is liked by the currently logged-in user.
     *
     * @param picture The picture to check for a like
     * @return boolean - Returns true if the picture is liked, false otherwise
     */
    public boolean isPictureLiked(Picture picture) {

        User user = getLoggedUser();// Get the logged-in user
        return user.getLikedPictures().contains(picture);// Return whether the picture is in the user's liked list
    }
}