package es.museotrapo.trapo.model;

public class Ticket {

    private String date;
    private Member owner;
    private int id;

    public Ticket(){}

    public Ticket(String date){
        this.date = date;
    }

    public Ticket(String date, Member owner, int id ){

        this.date = date;
        this.owner = owner;
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public Member getOwner() {
        return owner;
    }

    public int getId() {
        return id;
    }

    
}
