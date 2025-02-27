package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Member;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.Ticket;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MusseumService {

    private List<Picture> pictures = new ArrayList<>();
    private List<Artist> artists = new ArrayList<>();
    private List<Ticket> boughtTickets = new ArrayList<>();

    
    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Ticket> getBoughtTickets() {
        return boughtTickets;
    }

    public void setBoughtTickets(List<Ticket> boughtTickets) {
        this.boughtTickets = boughtTickets;
    }

    public void giveLikeTo(Member member, Picture picture){
        List<Member> newMemberLike = picture.getMemberLikes();
        List<Picture> newPictureLike = member.getLikedPictures();
        if(!newMemberLike.contains(member) && !newPictureLike.contains(picture)) {
            newMemberLike.add(member);
            newPictureLike.add(picture);
        }
        picture.setMemberLikes(newMemberLike);
        member.setLikedPictures(newPictureLike);
    }

/*
    public void purchaseTickets(Member member, String date){
        Ticket ticket;
        ticket = new Ticket(date, member);
        ticket.setOwner(member);
        member.setTicket(true);
    }

    public boolean hasTicket(Member member){
        return member.isTicket();
    }
*/
}
