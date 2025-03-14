package es.museotrapo.trapo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;


@Entity
public class Artist {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String nickname;
    private String birthDate;
    
    @OneToMany (mappedBy = "artist")
    private List<Picture> paintedPictures = new ArrayList<>();

    // Constructors
    public Artist() {
    }

    public Artist(String name) {
        this.name = name;
    }

    public Artist(String name, String nickname, String birthDate) {
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
    }

    // Getters & Setters
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public List<Picture> getPaintedPictures() {
        return paintedPictures;
    }

    public void setPaintedPictures(List<Picture> paintedPictures) {
        this.paintedPictures = paintedPictures;
    }
}
