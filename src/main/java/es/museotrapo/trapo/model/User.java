package es.museotrapo.trapo.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private Long id = 0L;
    private String name;
    private String email;

    private List<Comment> comments = new ArrayList<>();
    private List<Picture> likedPicture = new ArrayList<>();


    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<Picture> getLikedPosts() {
        return this.likedPicture;
    }

    public void setLikedPosts(List<Picture> likedPicture) {
        this.likedPicture = likedPicture;
    }

}
