package es.museotrapo.trapo;

import java.util.List;

public class Picture {
    private String name;
    private long id;
    private String imageFilename;
    private String date;
    private Artist author;
    private List<Member> memberLikes;

    public long getId() {
        return id;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public String getDate() {
        return date;
    }

    public Artist getAuthor() {
        return author;
    }

    public List<Member> getMemberLikes() {
        return memberLikes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAuthor(Artist author) {
        this.author = author;
    }

    public void setMemberLikes(List<Member> memberLikes) {
        this.memberLikes = memberLikes;
    }

    public void addMember(Member member) {
        memberLikes.add(member);
    }


}
