package es.museotrapo.trapo.model;

import jakarta.persistence.*;

import java.util.Objects;


@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String message;

    @ManyToOne
    //@JoinColumn(name = "author_id") // Columna FK en la tabla Comment
    private User author;

    @ManyToOne
    @JoinColumn(name = "picture_id", nullable = false) // Columna FK en la tabla Comment
    private Picture picture;


    // Constructors
    protected Comment() {
    }

    public Comment(String message) {
        this.message = message;
    }

    // Getters & Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNameAuthor() {
        return this.author.getName();
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Comment [id=" + id + ", author=" + (author != null ? author.getName() : "null") + ", message=" + message + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
