package es.museotrapo.trapo.service;

import es.museotrapo.trapo.dto.PictureDTO;
import es.museotrapo.trapo.dto.UserDTO;
import es.museotrapo.trapo.dto.UserMapper;
import es.museotrapo.trapo.exceptions.UserAlreadyExistsException;
import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.PictureRepository;
import es.museotrapo.trapo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private UserMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CommentService commentService;

    /**
     * Returns the logged-in username for simplicity. This is a placeholder method.
     * In a real application, it should be replaced with actual authentication
     * logic.
     *
     * @return User - The logged-in username
     */
    public UserDTO getLoggedUserDTO() {
        // For now, return the first username in the repository as the logged-in username
        return toDTO(getLoggedUser());
    }

    /**
     * Retrieves the logged-in user from the security context.
     *
     * @return User - The logged-in user
     */
    public User getLoggedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (SecurityContextHolder.getContext().getAuthentication() == null || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ||
                SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            return null;
        }

        return this.userRepository.findByName(username).get();
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return List<User> - A list of all users
     */
    public Collection<UserDTO> findAll() {
        return toDTOs(userRepository.findAll());
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id - The ID of the user to retrieve
     * @return User - The user with the specified ID
     */
    public UserDTO findById(Long id) {
        return toDTO(userRepository.findById(id).orElseThrow());
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username - The username of the user to retrieve
     * @return User - The user with the specified username
     */
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
        pictureRepository.save(picture);
        userRepository.save(user);// Save the updated user and picture back to the repository
    }

    /**
     * Checks if the logged-in user likes a specific picture.
     *
     * @param pictureDTO - The picture to check
     * @return boolean - True if the user likes the picture, false otherwise
     */
    public boolean isPictureLiked(PictureDTO pictureDTO) {
        User user = getLoggedUser();// Get the logged-in username
        if(user == null) return false;
        return user.getLikedPictures().contains(pictureRepository.findById(pictureDTO.id()).get());// Return whether the picture is in the username's liked list
    }

    /**
     * Checks if the logged-in user is an admin.
     *
     * @return boolean - True if the user is an admin, false otherwise
     */
    public UserDTO add(UserDTO userDTO, String password) {
        User user = toDomain(userDTO);
        if(userRepository.findByName(user.getName()).isPresent()) throw new UserAlreadyExistsException("Username already exists. Please choose another.");
        if (userRepository.findByEmail(user.getEmail()).isPresent()) throw new UserAlreadyExistsException("Email already exists. Please choose another.");
        user.setEncodedPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singletonList("USER"));
        userRepository.save(user);
        return toDTO(user);
    }

    /**
     * Checks if the logged-in user is an admin.
     *
     * @return boolean - True if the user is an admin, false otherwise
     */
    @Transactional
    public void remove(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        List<Picture> pictures = user.getLikedPictures();
        int num1 = pictures.size();
        for (int i = 0; i < num1; i++) {
            Picture picture = pictures.get(i);
            picture.getUserLikes().remove(user);
            user.getLikedPictures().remove(picture);
            pictureRepository.save(picture);
        }
        List<Comment> comments = user.getComments();
        int num = comments.size();
        for (int i = 0; i < num; i++) {
            Comment comment = comments.get(0);
            commentService.deleteCommentHelp(comment.getId(), comment.getPicture().getId());
        }
        userRepository.save(user);
        userRepository.deleteById(id);
    }

    /**
     * Checks if the logged-in user is an admin.
     *
     * @return boolean - True if the user is an admin, false otherwise
     */
    public UserDTO update(UserDTO userDTO, String password) {
        User oldUser = getLoggedUser();
        User newUser = toDomain(userDTO);

        newUser.setRoles(oldUser.getRoles());
        newUser.setEncodedPassword(passwordEncoder.encode(password));
        newUser.setLikedPictures(oldUser.getLikedPictures());
        newUser.setComments(oldUser.getComments());
        newUser.setId( oldUser.getId());
        userRepository.save(newUser);

        Authentication newAuth = new UsernamePasswordAuthenticationToken(toDTO(newUser), password);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return toDTO(newUser);
    }
    
    private UserDTO toDTO(User user) {
        return mapper.toDTO(user);
    }

    protected User toDomain(UserDTO userDTO) {
        return mapper.toDomain(userDTO);
    }

    private Collection<UserDTO> toDTOs(Collection<User> users) {
        return mapper.toDTOs(users);
    }
}