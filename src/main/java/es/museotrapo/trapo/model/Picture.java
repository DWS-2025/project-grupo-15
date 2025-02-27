package es.museotrapo.trapo.model;

import java.util.ArrayList;
import java.util.List;

public class Picture {


    private long id;
    private String name;
    private String imageFilename;
    private String description;
    private Artist author;

    private List<User> userLikes = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();


    public Picture(String name, String description, String imageFilename, Artist author){

        this.name = name;
        this.imageFilename = imageFilename;
        this.description = description;
        this.author = author;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setImageFilename(String imageFIlename) {
        this.imageFilename = imageFIlename;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(Artist author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public String getDescription() {
        return description;
    }

    public Artist getAuthor() {
        return author;
    }

    public void setUserLikesLikes(List<User> userLikes) {
        this.userLikes = userLikes;
    }

    public List<User> getUserLikes() {
        return userLikes;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return this.comments;
    }


}
