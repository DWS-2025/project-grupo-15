package es.museotrapo.trapo.controller;

import java.util.ArrayList;
import java.util.List;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.Ticket;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MusseumController {
    
    private List<Picture> pictures = new ArrayList<>();
    private List<Artist> artists = new ArrayList<>();
    private List<Ticket> boughtTickets = new ArrayList<>();

    @GetMapping("/")
    public String showAll(Model model){

        model.addAttribute("pictures", pictures);
        model.addAttribute("artists", artists);

        return "index";
    }

    @PostMapping("/pictures/predefined")
    public String predefinedPictures(Model model){
        
        pictures.add(new Picture("La Mona Lisa", "10 02 03", "https://th.bing.com/th/id/OIP.TPkYW_eMIbYQekiVd8_p6gAAAA?w=191&h=215&c=7&r=0&o=5&pid=1.7"));
        pictures.add(new Picture("La Mona Tiesa", "10 02 03", "https://th.bing.com/th?id=OIP.jOsfhX9fuPkNwu2SlzR4fgHaJ8&w=215&h=289&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2"));
        pictures.add(new Picture("La Mona Calva", "10 02 03", "https://th.bing.com/th?id=OIP.pJsoD_jzIuo83_6Tj-RiwgHaHa&w=250&h=250&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2"));

        return "redirect:/";
    }

    @PostMapping("/artists/predefined")
    public String predefinedArtists(Model model){

        artists.add(new Artist("Samuel Lopez", "DjSamsa", "No he nacido todavia"));
        artists.add(new Artist("Alejandro Sanchez", "AlexanderMaqueeen", "No he nacido todavia"));
        artists.add(new Artist("EL Random que se ha unido", "PutoRandom", "No he nacido todavia"));
        
        return "redirect:/";
    }

    @PostMapping("/picture/new")
    public String newPicture(Model model, Picture picture){
        
        pictures.add(picture);

        return "saved_picture";
    }

    @PostMapping("/artist/new")
    public String newArtist(Model model, Artist artist){

        artists.add(artist);

        return "saved_artist";
    }

    @PostMapping("/ticket/new")
    public String newTicket(Model model, Ticket ticket){

        boughtTickets.add(ticket);

        return "saved_ticket";
    }

    @GetMapping("pictures/{numPicture}")
    public String showPicture(Model model, @PathVariable int numPicture){

        Picture picture = pictures.get(numPicture - 1);
        model.addAttribute("picture", picture);
        model.addAttribute("numPicture", numPicture);

        return "show_picture";
    }

    @GetMapping("artists/{numArtist}")
    public String showArtist(Model model, @PathVariable int numArtist){

        Artist artist = artists.get(numArtist - 1);
        model.addAttribute("artist", artist);
        model.addAttribute("numArtist", numArtist);

        return "show_artist";
    }

    @PostMapping("/picture/{numPicture}/delete")
    public String deletePicture(Model model, @PathVariable int numPicture){

        pictures.remove(numPicture - 1);
        model.addAttribute("numPicture", numPicture);        
        
        return "deleted_picture";
    }

    @PostMapping("/artist/{numArtist}/delete")
    public String deleteArtist(Model model, @PathVariable int numArtist){

        artists.remove(numArtist - 1);
        model.addAttribute("numArtist", numArtist);        
        
        return "deleted_artist";
    }

    @PostMapping("/picture/{numPicture}/like")
    public String likePicture(Model model, @PathVariable int numPicture){

        Picture picture = pictures.get(numPicture - 1);
        //picture.giveLike(Member); Un metodo en el que se añade el like a la lista de likes del cuadro y se añade el cuadro a la lista de Member.
        //picture.quitLike(Member) lo mismo pero quitando el like.
        model.addAttribute("picture", picture);
        model.addAttribute("numPicture", numPicture);        
        
        return "liked_picture";
    }
}
