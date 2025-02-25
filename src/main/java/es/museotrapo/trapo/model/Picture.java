package es.museotrapo.trapo.model;

import java.util.ArrayList;
import java.util.List;

public class Picture {

    private String name;
    private String id;
    private String imageFilename;
    private String description;
    private Artist author;
    private List<Member> memberLikes = new ArrayList<>();
    

    public Picture(){}
    
    public Picture(String name, String description){
        this.name = name;
        this.description = description;
    }

    public Picture(String name, String description, String imageFilename){

        this.name = name;
        this.description = description;
        this.imageFilename = imageFilename;
    }

    public Picture(String name, String description, String imageFilename, Artist author){

        this.name = name;
        this.imageFilename = imageFilename;
        this.description = description;
        this.author = author;
    }

    public Picture(String name, String id, String imageFilename, String description, Artist author){

        this.name = name;
        this.id = id;
        this.imageFilename = imageFilename;
        this.description = description;
        this.author = author;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageFIlename(String imageFIlename) {
        this.imageFilename = imageFIlename;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public void setAuthor(Artist author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getImageFIlename() {
        return imageFilename;
    }

    public String getdescription() {
        return description;
    }

    public Artist getAuthor() {
        return author;
    }

    public void setMemberLikes(List<Member> memberLikes) {
        this.memberLikes = memberLikes;
    }

    public List<Member> getMemberLikes() {
        return memberLikes;
    }


}
