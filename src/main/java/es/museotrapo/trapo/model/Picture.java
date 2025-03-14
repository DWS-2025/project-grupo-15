package es.museotrapo.trapo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.engine.internal.Cascade;

@Entity
public class Picture {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    private String name;
    @Lob
    private String imageFilename;
    private String date;
    
    @ManyToOne
    private Artist artist;

    // Lists inside of a picture
    @ManyToMany
    private List<User> userLikes = new ArrayList<>(); // Users wich give like to the picture

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>(); // Comments in the picture

    // Constructors
    public Picture() {
    }

    public Picture(String name, String date) {
        super();
        this.name = name;
        this.date = date;
    }

    public Picture(String name, String date, String imageFilename, Artist artist) {
        this.name = name;
        this.imageFilename = imageFilename;
        this.date = date;
        this.artist = artist;
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

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
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
        return "Picture [id=" + id + ", name=" + name + ", date=" + date + ", artist=" + artist + "]";
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public void addLike(User like) {
        this.userLikes.add(like);
    }

    public void removeLike(User like) {
        this.userLikes.remove(like);
    }
}
