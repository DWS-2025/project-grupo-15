package es.museotrapo.trapo.model;

import java.util.List;

public class Artist {

    private long id;
    private String name;
    private String nickname;
    private String birthDate;
    private List<Picture> paintedPictures;


    public Artist(String name, String nickname, String birthDate){
        
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
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
