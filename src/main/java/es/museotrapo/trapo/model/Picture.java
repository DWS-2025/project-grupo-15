package es.museotrapo.trapo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Picture {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    private String name;
    private String imageFilename;
    private String date;
    private Artist author;

    // Lists inside of a picture
    private List<User> userLikes = new ArrayList<>(); // Users wich give like to the picture
    private List<Comment> comments = new ArrayList<>(); // Comments in the picture

    // Constructors
    public Picture() {
    }

    public Picture(String name, String date) {
        super();
        this.name = name;
        this.date = date;
    }

    public Picture(String name, String date, String imageFilename, Artist author) {
        this.name = name;
        this.imageFilename = imageFilename;
        this.date = date;
        this.author = author;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Artist getAuthor() {
        return author;
    }

    public void setAuthor(Artist author) {
        this.author = author;
    }

    public List<User> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(List<User> userLikes) {
        this.userLikes = userLikes;
    }

    public long getNumLikes() {
        return userLikes.size(); // Number of likes of the picture
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Picture [id=" + id + ", name=" + name + ", date=" + date + ", author=" + author + "]";
    }
}
