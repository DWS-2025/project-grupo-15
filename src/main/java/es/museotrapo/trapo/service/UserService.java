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
     * Returns always the same user for simplicity before adding authentication
     * @return User
     */
    public User getLoggedUser() {
        return userRepository.findAll().get(0);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void likeOrRemovePicture(long userId, Picture picture) {
        User user = getLoggedUser();
        if(user.getLikedPictures().contains(picture)) {
            user.getLikedPictures().remove(picture);
            picture.getUserLikes().remove(user);
        }else {
            user.getLikedPictures().add(picture);
            picture.getUserLikes().add(user);
        }
        userRepository.save(user);
    }

    public boolean isPictureLiked(Picture picture) {
        User user = getLoggedUser();
        return user.getLikedPictures().contains(picture);
    }
}
