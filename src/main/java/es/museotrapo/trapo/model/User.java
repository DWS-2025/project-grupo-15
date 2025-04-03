package es.museotrapo.trapo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.persistence.*;

@Entity
@Table(name = "USERTABLE")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = 0L;

    private String name;
    private String email;

    // Lists inside a User
    @ManyToMany (mappedBy = "userLikes", cascade = CascadeType.PERSIST)
    private List<Picture> likedPictures = new ArrayList<>();// List of all his liked pictures
    
    @OneToMany (mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();// List of comments in all pictures

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
        return this.likedPictures;
    }

    public void setLikedPictures(List<Picture> likedPictures) {
        this.likedPictures = likedPictures;
    }

    public List<String> getNameLikedPictures() {
        return this.likedPictures.stream().map(Picture::getName).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
