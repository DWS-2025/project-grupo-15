package es.museotrapo.trapo.model;

import java.util.List;

public class Member {

    private String realName;
    private String email;
    private String username;
    private List<Picture> likedPictures;

    public Member(String realName, String email, String username){
        
        this.realName = realName;
        this.email = email;
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
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

    public List<Picture> getLikedPictures() {
        return likedPictures;
    }

    public void setLikedPictures(List<Picture> likedPictures) {
        this.likedPictures = likedPictures;
    }
    
    
}
