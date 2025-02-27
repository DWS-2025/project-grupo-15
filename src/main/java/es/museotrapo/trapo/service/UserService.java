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

    public User getLoggedUser() {
        return userRepository.getUsers().get(0);
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public boolean isPictureLiked(Picture picture) {
        User user = getLoggedUser();
        return user.getLikedPosts().contains(picture);
    }

    public void likeOrRemovePicture(long userId, Picture picture) {
        User user = getLoggedUser();
        if(!isPictureLiked(picture)) {
            user.getLikedPosts().add(picture);
            picture.getUserLikes().add(user);
        }else {
            user.getLikedPosts().remove(picture);
            picture.getUserLikes().remove(user);
        }
        userRepository.save(user);
    }
}
