package es.museotrapo.trapo;

public class Ticket {
    private String date;
    private Member owner;
    private long id;

    private static long nextId = 1;

    public Ticket(String date, Member owner) {
        this.date = date;
        this.owner = owner;
        this.id = nextId++;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
