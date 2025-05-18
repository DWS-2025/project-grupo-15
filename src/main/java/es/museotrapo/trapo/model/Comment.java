package es.museotrapo.trapo.model;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Entity class representing a Comment.
 * This class maps to a database table using JPA (Java Persistence API) annotations
 * and represents a comment made by a user on a picture.
 */
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Automatically generates unique IDs for comments
    private long id;

    private String message; // The content of the comment

    @ManyToOne // Many comments can belong to one user
    //@JoinColumn(name = "author_id") // Optional: Specifies the column name for the author foreign key
    private User author;

    @ManyToOne
    @JoinColumn(name = "picture_id", nullable = false) // Specifies the column for the picture foreign key
    private Picture picture; // The picture this comment belongs to

    // ------------------------
    // Constructors
    // ------------------------

    /**
     * Protected no-arg constructor required by JPA.
     * Used for creating Comment instances without initialization.
     */
    protected Comment() {
    }

    /**
     * Constructor to create a comment with a specific message.
     *
     * @param message The text content of the comment.
     */
    public Comment(String message) {
        this.message = message;
    }

    // ------------------------
    // Getters and Setters
    // ------------------------

    /**
     * Gets the ID of the comment.
     *
     * @return The unique ID of the comment.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique ID of the comment.
     *
     * @param id The unique ID to set.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the message or text content of the comment.
     *
     * @return The comment's message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message or text content of the comment.
     *
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the name of the author who wrote the comment.
     *
     * @return The name of the author.
     */
    public String getNameAuthor() {
        return this.author.getName();
    }

    /**
     * Gets the user who authored the comment.
     *
     * @return The User object representing the author.
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Sets the author of the comment.
     *
     * @param author The User object representing the author.
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * Gets the picture that this comment is associated with.
     *
     * @return The Picture object representing the associated picture.
     */
    public Picture getPicture() {
        return picture;
    }

    /**
     * Sets the picture that this comment is linked to.
     *
     * @param picture The Picture object to be linked with this comment.
     */
    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    // ------------------------
    // Overridden Methods
    // ------------------------

    /**
     * Provides a string representation of the Comment object.
     * Includes the ID, author's name (if available), and the message.
     *
     * @return A string representation of the comment.
     */
    @Override
    public String toString() {
        return "Comment [id=" + id + ", author=" + (author != null ? author.getName() : "null") + ", message=" + message + "]";
    }

    /**
     * Checks equality between this comment and another object.
     * Two comments are considered equal if they share the same ID.
     *
     * @param o The object to compare with.
     * @return True if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id;
    }

    /**
     * Generates a hash code for the Comment object based on its ID.
     *
     * @return The hash code of the comment.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}