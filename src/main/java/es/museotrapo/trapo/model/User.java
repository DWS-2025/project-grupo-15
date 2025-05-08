package es.museotrapo.trapo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "USERTABLE")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = 0L;

    private String name;
    private String email;

    private String encodedPassword;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    // Lists inside a User
    @ManyToMany(mappedBy = "userLikes", cascade = CascadeType.PERSIST)
    private List<Picture> likedPictures = new ArrayList<>();// List of all his liked pictures

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();// List of comments in all pictures

    // Constructors
    public User() {
    }

    public User(String name, String email, String encodedPassword, String... roles) {
        this.name = name;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.roles = List.of(roles);
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
        List<String> nameLikedPictures = new ArrayList<>();
        for (Picture picture : likedPictures) {
            nameLikedPictures.add(picture.getName());
        }
        return nameLikedPictures;
    }

    public List<String> getCommentsMessage() {
        List<String> comments = new ArrayList<>();
        for (Comment comment : this.comments) {
            comments.add(comment.getMessage());
        }
        return comments;
    }
    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public List<String> getRoles(){
        return this.roles;
    }
    public void setRoles(List<String> roles){
        this.roles = roles;
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
