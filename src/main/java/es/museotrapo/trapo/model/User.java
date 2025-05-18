package es.museotrapo.trapo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

/**
 * Entity class representing a User.
 * This class maps to a database table using JPA annotations and represents a user in the application.
 * Users can like pictures, comment on them, and have assigned roles for access control.
 */
@Entity
@Table(name = "USERTABLE") // Specifies the database table name as "USERTABLE".
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Automatically generates unique IDs for users.
    private Long id = 0L; // Primary key for the User entity.

    private String name; // User's name.
    private String email; // User's email address (used for authentication and communication).

    private String encodedPassword; // Encoded (hashed) password for security.

    @ElementCollection(fetch = FetchType.EAGER) // Fetches the collection of roles eagerly.
    private List<String> roles; // List of roles assigned to the user (e.g., ROLE_USER, ROLE_ADMIN).

    // Relationships
    @ManyToMany(mappedBy = "userLikes", cascade = CascadeType.PERSIST) // Many-to-many relationship with Picture.
    private List<Picture> likedPictures = new ArrayList<>(); // List of pictures liked by the user.

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL) // One-to-many relationship with Comment.
    private List<Comment> comments = new ArrayList<>(); // List of comments made by the user.

    // ------------------------
    // Constructors
    // ------------------------

    /**
     * Default no-arg constructor required by JPA.
     */
    public User() {
    }

    /**
     * Constructor to initialize a user with name, email, password, and roles.
     *
     * @param name           The name of the user.
     * @param email          The email of the user.
     * @param encodedPassword The encoded (hashed) password for the user.
     * @param roles          The roles assigned to the user.
     */
    public User(String name, String email, String encodedPassword, String... roles) {
        this.name = name;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.roles = List.of(roles); // Converts the varargs to an immutable list of roles.
    }

    // ------------------------
    // Getters and Setters
    // ------------------------

    /**
     * Gets the unique ID of the user.
     *
     * @return The user's unique ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique ID of the user.
     *
     * @param id The unique ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the user's name.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's name.
     *
     * @param name The name of the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the user's email address.
     *
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the list of comments made by the user.
     *
     * @return A list of Comment objects authored by the user.
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * Sets the list of comments made by the user.
     *
     * @param comments A list of Comment objects authored by the user.
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Gets the list of pictures liked by the user.
     *
     * @return A list of Picture objects liked by the user.
     */
    public List<Picture> getLikedPictures() {
        return this.likedPictures;
    }

    /**
     * Sets the list of liked pictures for the user.
     *
     * @param likedPictures A list of Picture objects.
     */
    public void setLikedPictures(List<Picture> likedPictures) {
        this.likedPictures = likedPictures;
    }

    /**
     * Gets the names of the pictures liked by the user.
     *
     * @return A list of names (Strings) of the liked pictures.
     */
    public List<String> getNameLikedPictures() {
        List<String> nameLikedPictures = new ArrayList<>();
        for (Picture picture : likedPictures) {
            nameLikedPictures.add(picture.getName());
        }
        return nameLikedPictures;
    }

    /**
     * Gets the messages from the comments authored by the user.
     *
     * @return A list of strings representing the comment messages.
     */
    public List<String> getCommentsMessage() {
        List<String> comments = new ArrayList<>();
        for (Comment comment : this.comments) {
            comments.add(comment.getMessage());
        }
        return comments;
    }

    /**
     * Gets the user's encoded (hashed) password.
     *
     * @return The encoded password.
     */
    public String getEncodedPassword() {
        return encodedPassword;
    }

    /**
     * Sets the user's encoded (hashed) password.
     *
     * @param encodedPassword The encoded password to set.
     */
    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    /**
     * Gets the list of roles assigned to the user.
     *
     * @return A list of roles as Strings.
     */
    public List<String> getRoles() {
        return this.roles;
    }

    /**
     * Sets the list of roles for the user.
     *
     * @param roles A list of roles as Strings.
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    // ------------------------
    // Overridden Methods
    // ------------------------

    /**
     * Checks equality between this user and another object.
     * Two users are considered equal if their IDs are the same.
     *
     * @param o The object to compare with.
     * @return True if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    /**
     * Generates a hash code for the User object based on its ID.
     *
     * @return The hash code of the user.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}