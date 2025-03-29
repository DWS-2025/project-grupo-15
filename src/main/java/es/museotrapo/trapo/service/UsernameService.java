package es.museotrapo.trapo.service;

import es.museotrapo.trapo.dto.PictureDTO;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.PictureRepository;
import es.museotrapo.trapo.repository.UsernameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsernameService {

    @Autowired
    private UsernameRepository usernameRepository;
    @Autowired
    private PictureRepository pictureRepository;

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

        Picture picture = pictureRepository.findById(pictureDTO.id()).get();
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

    public boolean isPictureLiked(PictureDTO pictureDTO) {


        User user = getLoggedUser();// Get the logged-in username
        return user.getLikedPictures().contains(pictureRepository.findById(pictureDTO.id()).get());// Return whether the picture is in the username's liked list
    }

}