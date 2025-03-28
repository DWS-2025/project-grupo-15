package es.museotrapo.trapo.model;

import jakarta.persistence.*;


@Entity
public class Comment {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String message;

    @ManyToOne
    private User author;

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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Comment [id=" + id + ", author=" + author.getName() + ", message=" + message + "]";
    }
}
