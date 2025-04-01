package es.museotrapo.trapo.RESTcontroller;

import es.museotrapo.trapo.dto.PictureDTO;
import es.museotrapo.trapo.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/api/pictures")
public class PictureControllerREST {

    @Autowired
    private PictureService pictureService; // Service to handle picture-related functionality


    @GetMapping("/")
    public Collection <PictureDTO> getPictures() {

        return pictureService.getPictures();
    }

    @GetMapping("/{id}")
    public PictureDTO getPicture(@PathVariable long id) {

        return pictureService.getPicture(id);
    }

    @DeleteMapping("/{id}")
	public PictureDTO deletePicture(@PathVariable long id) {

        return pictureService.deletePicture(getPicture(id));
	}

}
