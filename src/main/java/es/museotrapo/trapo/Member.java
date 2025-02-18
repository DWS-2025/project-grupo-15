package es.museotrapo.trapo;

import java.util.List;
import java.util.Objects;

public class Member {
    private String realName;
    private String email;
    private String username;
    private List<Picture> likedPictures;

    public String getRealName() {
        return realName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public List<Picture> getLikedPictures() {
        return likedPictures;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLikedPictures(List<Picture> likedPictures) {
        this.likedPictures = likedPictures;
    }

    public void add(Picture picture) {
        likedPictures.add(picture);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(username, member.username);
    }

}
