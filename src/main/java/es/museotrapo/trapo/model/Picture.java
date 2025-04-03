package es.museotrapo.trapo.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Picture {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    private String name;
    private String date;

    private String image;

    @Lob
    @JsonIgnore
    private Blob imageFile;
    
    @ManyToOne
    private Artist artist;

    // Lists inside of a picture
    @ManyToMany(cascade = CascadeType.MERGE)
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

    public Picture(String name, String date, Blob imageFile, Artist artist) {
        this.name = name;
        this.imageFile = imageFile;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
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

    public List<Comment> getComments() {
        return this.comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getArtistNickname() {
        return this.artist.getNickname();
    }

    public Long getArtistId() {
        return this.artist.getId();
    }

    public List<String> getNameUserLikes() {
        List<String> nameUserLikes = new ArrayList<>();
        for(User user: this.userLikes){
            nameUserLikes.add(user.getName());
        }
        return nameUserLikes;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return id == picture.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
