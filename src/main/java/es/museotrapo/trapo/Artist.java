package es.museotrapo.trapo;

import java.util.List;

public class Artist {
    private String name;
    private String nickname;
    private String birthDate;
    private List<Picture> paintedPictures;

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public List<Picture> getPaintedPictures() {
        return paintedPictures;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setPaintedPictures(List<Picture> paintedPictures) {
        this.paintedPictures = paintedPictures;
    }
}
