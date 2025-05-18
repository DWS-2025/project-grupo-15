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

/**
 * Service class for managing User operations.
 * Provides functionality for user management, authentication, liking pictures, and handling relationships with other entities.
 */
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
     * Retrieves the currently logged-in user as a UserDTO.
     *
     * @return UserDTO - A Data Transfer Object containing the logged-in user's details.
     */
    public UserDTO getLoggedUserDTO() {
        return toDTO(getLoggedUser());
    }

    /**
     * Retrieves the currently logged-in user as a User entity.
     *
     * @return User - The logged-in user or null if no user is authenticated.
     */
    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // If there is no valid authentication or the user is anonymous, return null.
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        // Retrieve the username from the authentication context and fetch the user from the database.
        String username = authentication.getName();
        return userRepository.findByName(username).orElse(null);
    }

    /**
     * Retrieves all users from the database and converts them to a collection of UserDTOs.
     *
     * @return Collection<UserDTO> - All users in the system as DTOs.
     */
    public Collection<UserDTO> findAll() {
        return toDTOs(userRepository.findAll());
    }

    /**
     * Retrieves a user by a specific ID and converts it to a UserDTO.
     *
     * @param id - The ID of the user to retrieve.
     * @return UserDTO - The data transfer object representing the user.
     */
    public UserDTO findById(Long id) {
        return toDTO(userRepository.findById(id).orElseThrow());
    }

    /**
     * Allows a user to like or remove a like from a specific picture.
     *
     * @param pictureDTO - Data Transfer Object representing the picture being liked or unliked.
     */
    public void likeOrRemovePicture(PictureDTO pictureDTO) {
        Picture picture = pictureRepository.findById(pictureDTO.id()).orElseThrow();
        User user = getLoggedUser(); // Get the currently logged-in user.

        // Check if the user has already liked the picture.
        if (user.getLikedPictures().contains(picture)) {
            // If already liked, remove the like.
            user.getLikedPictures().remove(picture);
            picture.getUserLikes().remove(user);
        } else {
            // If not already liked, add the like.
            user.getLikedPictures().add(picture);
            picture.getUserLikes().add(user);
        }

        // Save the updated entities to the database.
        pictureRepository.save(picture);
        userRepository.save(user);
    }

    /**
     * Determines if the currently logged-in user has liked a specific picture.
     *
     * @param pictureDTO - Data Transfer Object representing the picture.
     * @return boolean - True if the picture is liked by the user, false otherwise.
     */
    public boolean isPictureLiked(PictureDTO pictureDTO) {
        User user = getLoggedUser();
        if (user == null) return false;
        return user.getLikedPictures().contains(pictureRepository.findById(pictureDTO.id()).orElseThrow());
    }

    /**
     * Adds a new user to the repository with encrypted password and default roles.
     *
     * @param userDTO  - Data Transfer Object containing user data.
     * @param password - The plaintext password to encode.
     * @return UserDTO - The newly created user's data as a DTO.
     * @throws UserAlreadyExistsException if the username or email already exists.
     */
    public UserDTO add(UserDTO userDTO, String password) {
        User user = toDomain(userDTO);

        // Check if the username or email already exists in the database.
        if (userRepository.findByName(user.getName()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists. Please choose another.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists. Please choose another.");
        }

        // Encode the user's password and assign default roles.
        user.setEncodedPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singletonList("USER"));

        // Save the user and return their data as a DTO.
        userRepository.save(user);
        return toDTO(user);
    }

    /**
     * Deletes a user by their ID. Also removes all relationships (liked pictures and comments).
     *
     * @param id - The ID of the user to delete.
     */
    @Transactional
    public void remove(Long id) {
        User user = userRepository.findById(id).orElseThrow();

        // Remove all pictures liked by the user.
        List<Picture> pictures = user.getLikedPictures();
        for (Picture picture : pictures) {
            picture.getUserLikes().remove(user);
            // Save changes to each picture.
            pictureRepository.save(picture);
        }
        user.getLikedPictures().clear();

        // Delete all comments written by the user.
        List<Comment> comments = user.getComments();
        for (Comment comment : comments) {
            commentService.deleteCommentHelp(comment.getId(), comment.getPicture().getId());
        }
        user.getComments().clear();

        // Delete the user record from the repository.
        userRepository.deleteById(id);
    }

    /**
     * Updates the logged-in user's details with new data and password.
     *
     * @param userDTO  - The updated user data as a DTO.
     * @param password - The new password to set for the user.
     * @return UserDTO - The updated user's data as a DTO.
     */
    public UserDTO update(UserDTO userDTO, String password) {
        User oldUser = getLoggedUser();
        User newUser = toDomain(userDTO);

        // Preserve roles, liked pictures, and comments from the old user.
        newUser.setRoles(oldUser.getRoles());
        newUser.setEncodedPassword(passwordEncoder.encode(password));
        newUser.setLikedPictures(oldUser.getLikedPictures());
        newUser.setComments(oldUser.getComments());
        newUser.setId(oldUser.getId());

        // Save the updated user to the repository.
        userRepository.save(newUser);

        // Refresh the user's authentication with new credentials.
        Authentication newAuth = new UsernamePasswordAuthenticationToken(toDTO(newUser), password);
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return toDTO(newUser);
    }

    /**
     * Converts a User entity to a UserDTO for easier data transfer.
     *
     * @param user - The User entity to convert.
     * @return UserDTO - The converted UserDTO.
     */
    private UserDTO toDTO(User user) {
        return mapper.toDTO(user);
    }

    /**
     * Converts a UserDTO to a User entity for database operations.
     *
     * @param userDTO - The UserDTO to convert.
     * @return User - The converted User entity.
     */
    protected User toDomain(UserDTO userDTO) {
        return mapper.toDomain(userDTO);
    }

    /**
     * Converts a collection of User entities to a collection of UserDTOs.
     *
     * @param users - A collection of User entities.
     * @return Collection<UserDTO> - The converted collection of UserDTOs.
     */
    private Collection<UserDTO> toDTOs(Collection<User> users) {
        return mapper.toDTOs(users);
    }
}