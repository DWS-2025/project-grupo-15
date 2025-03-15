package es.museotrapo.trapo.model;

import java.lang.reflect.Member;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Picture {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    private String name;
    private String date;

    @Lob
    private Blob imageFile;
    
    @ManyToOne
    private Artist artist;

    // Lists inside of a picture
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<Username> usernameLikes = new ArrayList<>(); // Users wich give like to the picture

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

    public List<Username> getUserLikes() {
        return usernameLikes;
    }

    public void setUserLikes(List<Username> usernameLikes) {
        this.usernameLikes = usernameLikes;
    }

    public long getNumLikes() {
        return usernameLikes.size(); // Number of likes of the picture
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
}
