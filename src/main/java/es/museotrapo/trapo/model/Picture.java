package es.museotrapo.trapo.model;

import java.util.List;

public class Picture {

    private String name;
    private String id;
    private String imageFilename;
    private String date;
    private Artist author;
    private List<Member> memberLikes;
    

    public Picture(){}
    
    public Picture(String name, String date){
        this.name = name;
        this.date = date;
    }

    public Picture(String name, String date, String imageFilename){

        this.name = name;
        this.date = date;
        this.imageFilename = imageFilename;
    }

    public Picture(String name, String id, String imageFilename, String date, Artist author){

        this.name = name;
        this.id = id;
        this.imageFilename = imageFilename;
        this.date = date;
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

    public void setDate(String date) {
        this.date = date;
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

    public String getDate() {
        return date;
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
