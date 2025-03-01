package es.museotrapo.trapo.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private Long id = 0L;
    private String name;
    private String email;

    // Lists inside a User
    private List<Comment> comments = new ArrayList<>();// List of comments in all pictures
    private List<Picture> likedPicture = new ArrayList<>();// List of all his liked pictures

    // Constructors
    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Picture> getLikedPictures() {
        return likedPicture;
    }

    public void setLikedPictures(List<Picture> likedPicture) {
        this.likedPicture = likedPicture;
    }
}
