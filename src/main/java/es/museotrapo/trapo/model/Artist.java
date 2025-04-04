package es.museotrapo.trapo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;


@Entity
public class Artist {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String nickname;
    private String birthDate;
    
    @OneToMany (mappedBy = "artist", cascade = CascadeType.ALL)
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
        return this.paintedPictures;
    }

    public void setPaintedPictures(List<Picture> paintedPictures) {
        this.paintedPictures = paintedPictures;
    }

    public List<String> getNamePaintedPictures() {
        List<String> namePaintedPictures = new ArrayList<>();
        for (Picture picture : this.paintedPictures) {
            namePaintedPictures.add(picture.getName());
        }
        return namePaintedPictures;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id == artist.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
