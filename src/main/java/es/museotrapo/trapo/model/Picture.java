package es.museotrapo.trapo.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Entity class representing a Picture.
 * Maps to a database table using JPA (Java Persistence API) annotations.
 * Each picture can have an associated artist, likes from users, and comments.
 */
@Entity
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Automatically generates unique IDs for pictures
    long id;

    private String name;      // Name of the picture
    private String date;      // Date when the picture was created

    private String image;     // Image file path or URL

    @Lob // Indicates that this field represents a large object (LOB)
    @JsonIgnore // Prevents this field from being serialized when returned in a JSON response
    private Blob imageFile;   // Raw binary content of the image

    @ManyToOne // Many pictures can belong to one artist
    private Artist artist;    // Artist who created the picture

    // Relationships
    @ManyToMany(cascade = CascadeType.MERGE) // Many-to-many relationship with users
    private List<User> userLikes = new ArrayList<>(); // List of Users who liked the picture

    @OneToMany(cascade = CascadeType.ALL) // One-to-many relationship with comments
    private List<Comment> comments = new ArrayList<>(); // List of comments on the picture

    // ------------------------
    // Constructors
    // ------------------------

    /**
     * Default no-arg constructor required by JPA.
     * Used to create empty Picture objects.
     */
    public Picture() {
    }

    /**
     * Constructor to initialize a picture with its name and date.
     *
     * @param name The name of the picture.
     * @param date The creation date of the picture.
     */
    public Picture(String name, String date) {
        super();
        this.name = name;
        this.date = date;
    }

    /**
     * Constructor to initialize a picture with its name, date, image file, and artist.
     *
     * @param name      The name of the picture.
     * @param date      The creation date of the picture.
     * @param imageFile The raw binary content of the image.
     * @param artist    The artist who created the picture.
     */
    public Picture(String name, String date, Blob imageFile, Artist artist) {
        this.name = name;
        this.imageFile = imageFile;
        this.date = date;
        this.artist = artist;
    }

    // ------------------------
    // Getters and Setters
    // ------------------------

    /**
     * Gets the ID of the picture.
     *
     * @return The unique ID of the picture.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the stored image file path or URL as a string.
     *
     * @return The image string representation.
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the stored image file path or URL as a string.
     *
     * @param image The image string representation.
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Gets the name of the picture.
     *
     * @return The name of the picture.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the picture.
     *
     * @param name The name of the picture.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the raw binary content of the image file.
     *
     * @return The Blob representing the image.
     */
    public Blob getImageFile() {
        return imageFile;
    }

    /**
     * Sets the raw binary content of the image file.
     *
     * @param imageFile The Blob object for the image file.
     */
    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * Gets the date the picture was created.
     *
     * @return The creation date of the picture.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date the picture was created.
     *
     * @param date The creation date of the picture.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the artist who created the picture.
     *
     * @return The Artist object associated with the picture.
     */
    public Artist getArtist() {
        return artist;
    }

    /**
     * Sets the artist who created the picture.
     *
     * @param artist The Artist object to associate with the picture.
     */
    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    /**
     * Gets the list of users who liked the picture.
     *
     * @return A list of User objects who liked the picture.
     */
    public List<User> getUserLikes() {
        return userLikes;
    }

    /**
     * Sets the list of users who liked the picture.
     *
     * @param userLikes A list of User objects who liked the picture.
     */
    public void setUserLikes(List<User> userLikes) {
        this.userLikes = userLikes;
    }

    /**
     * Gets the list of comments on the picture.
     *
     * @return A list of Comment objects on the picture.
     */
    public List<Comment> getComments() {
        return this.comments;
    }

    /**
     * Sets the list of comments on the picture.
     *
     * @param comments A list of Comment objects to associate with the picture.
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Gets the nickname of the artist who created the picture.
     *
     * @return The nickname of the artist.
     */
    public String getArtistNickname() {
        return this.artist.getNickname();
    }

    /**
     * Gets the ID of the artist who created the picture.
     *
     * @return The unique ID of the artist.
     */
    public Long getArtistId() {
        return this.artist.getId();
    }

    /**
     * Gets the list of names of all users who liked the picture.
     *
     * @return A list of string names of users who liked the picture.
     */
    public List<String> getNameUserLikes() {
        List<String> nameUserLikes = new ArrayList<>();
        for (User user : this.userLikes) {
            nameUserLikes.add(user.getName());
        }
        return nameUserLikes;
    }

    /**
     * Gets the list of all comment messages on the picture.
     *
     * @return A list of strings representing the messages of the comments.
     */
    public List<String> getCommentsMessage() {
        List<String> commentsMessage = new ArrayList<>();
        for (Comment comment : this.comments) {
            commentsMessage.add(comment.getMessage());
        }
        return commentsMessage;
    }

    // ------------------------
    // Overridden Methods
    // ------------------------

    /**
     * Checks equality between this picture and another object.
     * Two pictures are considered equal if they have the same ID.
     *
     * @param o The object to compare with.
     * @return True if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return id == picture.id;
    }

    /**
     * Generates a hash code for the Picture object based on its ID.
     *
     * @return The hash code of the picture.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}