package es.museotrapo.trapo.controller;

import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;

import es.museotrapo.trapo.service.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
@RequestMapping("/picture")
public class PictureController {

    private static final Path PICTURE_PATH = Paths.get(System.getProperty("user.dir"), "pictures");

    @Autowired
    private PictureService pictureService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ImageService imageService;

    @GetMapping("")
    public String getPosts(Model model){
        model.addAttribute("pictures", pictureService.findAll());
        return "pictures";
    }

    @GetMapping("/new")
    public String newPicture(Model model) {
        model.addAttribute("availableArtists", artistService.findAll());
        return "new_picture";
    }

    @PostMapping("/new")
    public String newPicture(Model model,
                             Picture picture,
                             @RequestParam MultipartFile imageFile,
                             @RequestParam Long artistID) throws IOException {

        if(picture.getDate() == null || picture.getName() == null || artistID == null) {
            throw new IllegalArgumentException("NO pueden haber campos vacios");
        }
        picture.setImageFilename(imageService.createImage(imageFile));
        pictureService.save(picture, artistID);
        artistService.addPicture(artistID, picture);

        model.addAttribute("picture", picture);

        return "saved_picture";
    }

    @GetMapping("/{id}")
    public String getPost(Model model, @PathVariable long id) {
        Optional<Picture> picture = pictureService.findById(id);
        if (picture.isPresent()) {
            model.addAttribute("picture", picture.get());
            String likedPicture = userService.isPictureLiked(picture.get()) ? "Dislike" : "Like";
            model.addAttribute("likedPicture", likedPicture);
            model.addAttribute("imagePath", "/picture/" + picture.get().getImageFilename());
            //model.addAttribute("numLikes", picture.get().getNumLikes());
            return "show_picture";
        } else {
            return "picture_not_found";
        }
    }

    @GetMapping("/image/{ImageFilename}")
    public ResponseEntity<Resource> getImage(@PathVariable String ImageFilename) throws MalformedURLException {
        Path imagePath = PICTURE_PATH.resolve(ImageFilename);
        Resource image = new UrlResource(imagePath.toUri());

        if (image.exists() && image.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // Ajusta el tipo MIME según el formato
                    .body(image);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable long id) {
        Optional<Picture> picture = pictureService.findById(id);
        if (picture.isPresent()) {
            pictureService.delete(picture.get());
            return "deleted_picture";
        } else {
            return "picture_not_found";
        }
    }

    @PostMapping("/{picId}/comments/new")
    public String newComment(@PathVariable long picId, Comment comment) {
        Optional<Picture> picture = pictureService.findById(picId);
        if (picture.isPresent()) {
            Picture picture1 = picture.get();
            commentService.save(picture1, comment);
            return "redirect:/picture/" + picId;
        } else {
            return "picture_not_found";
        }
    }

    @PostMapping("/{picId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable long picId, @PathVariable long commentId) {

        Optional<Picture> picture = pictureService.findById(picId);

        if (picture.isPresent()) {
            Picture picture1 = picture.get();
            commentService.delete(commentId, picture1);
            return "redirect:/picture/" + picId;
        } else {
            return "picture_not_found";
        }

    }

    @PostMapping("/{picId}/likeToggle")
    public String likePicture(@PathVariable Long picId, Long userId) {
        Optional<Picture> picture = pictureService.findById(picId);
        if (picture.isPresent()) {
            Picture picture1 = picture.get();
            userService.likeOrRemovePicture(userId, picture1);
            return "redirect:/picture/" + picId;
        } else {
            return "picture_not_found";
        }
    }
}
