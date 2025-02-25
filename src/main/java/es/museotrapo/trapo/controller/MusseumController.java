package es.museotrapo.trapo.controller;


import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.Ticket;
import es.museotrapo.trapo.service.MusseumService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MusseumController {
    

    private final MusseumService service;

    public MusseumController(MusseumService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String showAll(Model model){
        model.addAttribute("pictures", service.getPictures());
        model.addAttribute("artists", service.getArtists());

        return "index";
    }

    @PostMapping("/pictures/predefined")
    public String predefinedPictures(Model model){;
        service.getPictures().add(new Picture("La Mona Lisa", "10 02 03", "https://th.bing.com/th/id/OIP.TPkYW_eMIbYQekiVd8_p6gAAAA?w=191&h=215&c=7&r=0&o=5&pid=1.7"));
        service.getPictures().add(new Picture("La Mona Tiesa", "10 02 03", "https://th.bing.com/th?id=OIP.jOsfhX9fuPkNwu2SlzR4fgHaJ8&w=215&h=289&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2"));
        service.getPictures().add(new Picture("La Mona Calva", "10 02 03", "https://th.bing.com/th?id=OIP.pJsoD_jzIuo83_6Tj-RiwgHaHa&w=250&h=250&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2"));

        return "redirect:/";
    }

    @PostMapping("/artists/predefined")
    public String predefinedArtists(Model model){

        service.getArtists().add(new Artist("Samuel Lopez", "DjSamsa", "No he nacido todavia"));
        service.getArtists().add(new Artist("Alejandro Sanchez", "AlexanderMaqueeen", "No he nacido todavia"));
        service.getArtists().add(new Artist("EL Random que se ha unido", "PutoRandom", "No he nacido todavia"));
        
        return "redirect:/";
    }

    @PostMapping("/picture/new")
    public String newPicture(Model model, Picture picture){
        
        service.getPictures().add(picture);

        return "saved_picture";
    }

    @PostMapping("/artist/new")
    public String newArtist(Model model, Artist artist){

        service.getArtists().add(artist);

        return "saved_artist";
    }

    @PostMapping("/ticket/new")
    public String newTicket(Model model, Ticket ticket){

        service.getBoughtTickets().add(ticket);

        return "saved_ticket";
    }

    @GetMapping("pictures/{numPicture}")
    public String showPicture(Model model, @PathVariable int numPicture){

        Picture picture = service.getPictures().get(numPicture - 1);
        model.addAttribute("picture", picture);
        model.addAttribute("numPicture", numPicture);

        return "show_picture";
    }

    @GetMapping("artists/{numArtist}")
    public String showArtist(Model model, @PathVariable int numArtist){

        Artist artist = service.getArtists().get(numArtist - 1);
        model.addAttribute("artist", artist);
        model.addAttribute("numArtist", numArtist);

        return "show_artist";
    }

    @PostMapping("/picture/{numPicture}/delete")
    public String deletePicture(Model model, @PathVariable int numPicture){

        service.getPictures().remove(numPicture - 1);
        model.addAttribute("numPicture", numPicture);        
        
        return "deleted_picture";
    }

    @PostMapping("/artist/{numArtist}/delete")
    public String deleteArtist(Model model, @PathVariable int numArtist){

        service.getArtists().remove(numArtist - 1);
        model.addAttribute("numArtist", numArtist);        
        
        return "deleted_artist";
    }

    @PostMapping("/picture/{numPicture}/like")
    public String likePicture(Model model, @PathVariable int numPicture){

        Picture picture = service.getPictures().get(numPicture - 1);
        //picture.giveLike(Member); Un metodo en el que se añade el like a la lista de likes del cuadro y se añade el cuadro a la lista de Member.
        //picture.quitLike(Member) lo mismo pero quitando el like.
        model.addAttribute("picture", picture);
        model.addAttribute("numPicture", numPicture);        
        
        return "liked_picture";
    }
}
