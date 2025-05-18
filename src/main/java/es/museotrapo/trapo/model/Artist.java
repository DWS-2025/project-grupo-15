package es.museotrapo.trapo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

/**
 * Entity class representing an Artist.
 * Maps to a database table using JPA (Java Persistence API) annotations.
 * Each artist can have multiple associated pictures they have created.
 */
@Entity
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Auto-generates the primary key values
    private long id;

    private String biography; // Artist's biography
    private String name;      // Artist's full name
    private String nickname;  // Artist's nickname or alias
    private String birthDate; // Artist's date of birth

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL) // One-to-many relationship with Picture
    private List<Picture> paintedPictures = new ArrayList<>();

    // ------------------------
    // Constructors
    // ------------------------

    /**
     * Default constructor for JPA. Required for creating entities.
     */
    public Artist() {
    }

    /**
     * Constructor to initialize an artist with a name.
     *
     * @param name The name of the artist.
     */
    public Artist(String name) {
        this.name = name;
    }

    /**
     * Constructor to initialize an artist with name, nickname, and birth date.
     *
     * @param name      The artist's name.
     * @param nickname  The artist's nickname.
     * @param birthDate The artist's birth date.
     */
    public Artist(String name, String nickname, String birthDate) {
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
    }

    // ------------------------
    // Getters and Setters
    // ------------------------

    /**
     * Gets the ID of the artist.
     *
     * @return The unique ID of the artist.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the artist's ID.
     *
     * @param id The unique ID of the artist.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the artist's full name.
     *
     * @return The name of the artist.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the artist's full name.
     *
     * @param name The name of the artist.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the artist's nickname.
     *
     * @return The nickname of the artist.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the artist's nickname.
     *
     * @param nickname The nickname of the artist.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the artist's birth date.
     *
     * @return The birth date of the artist.
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the artist's birth date.
     *
     * @param birthDate The birth date of the artist.
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Gets the list of pictures painted by the artist.
     *
     * @return A list of Picture objects associated with the artist.
     */
    public List<Picture> getPaintedPictures() {
        return this.paintedPictures;
    }

    /**
     * Sets the list of pictures associated with the artist.
     *
     * @param paintedPictures The list of Picture objects.
     */
    public void setPaintedPictures(List<Picture> paintedPictures) {
        this.paintedPictures = paintedPictures;
    }

    /**
     * Gets the names of all the pictures painted by the artist.
     *
     * @return A list of picture names as Strings.
     */
    public List<String> getNamePaintedPictures() {
        List<String> namePaintedPictures = new ArrayList<>();
        for (Picture picture : this.paintedPictures) {
            namePaintedPictures.add(picture.getName());
        }
        return namePaintedPictures;
    }

    /**
     * Gets the artist's biography.
     *
     * @return The biography of the artist.
     */
    public String getBiography() {
        return biography;
    }

    /**
     * Sets the artist's biography.
     *
     * @param biography The biography of the artist.
     */
    public void setBiography(String biography) {
        this.biography = biography;
    }

    // ------------------------
    // Overridden Methods
    // ------------------------

    /**
     * Checks equality between this artist and another object.
     * Two artists are considered equal if they have the same ID.
     *
     * @param o The object to compare with.
     * @return True if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id == artist.id;
    }

    /**
     * Generates a hash code for the artist object based on its ID.
     *
     * @return The hash code of the artist.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}