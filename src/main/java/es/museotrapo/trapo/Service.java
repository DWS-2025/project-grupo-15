package es.museotrapo.trapo;

import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;

import java.util.List;

public class Service {

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

    public void purchaseTickets(Member member, String date){
        Ticket ticket;
        ticket = new Ticket(date, member);
        ticket.setOwner(member);
        member.setTicket(true);
    }

    public boolean hasTicket(Member member){
        return member.isTicket();
    }

    
}
