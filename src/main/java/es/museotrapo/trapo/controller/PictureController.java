package es.museotrapo.trapo.controller;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import es.museotrapo.trapo.service.ArtistService;
import es.museotrapo.trapo.service.CommentService;
import es.museotrapo.trapo.service.PictureService;
import es.museotrapo.trapo.service.UserService;
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

    private static final Path PICTURE_PATH = Paths.get(System.getProperty("user.dir"), "picture");

    @Autowired
    private PictureService pictureService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @GetMapping("")
    public String getPosts(Model model){
        model.addAttribute("Pictures", pictureService.findAll());
        return "pictures";
    }

    @GetMapping("/new")
    public String newPicture(Model model) {
        return "new_picture";
    }

    @PostMapping("/new")
    public String newPicture(@RequestParam("name") String name,
                             @RequestParam("date") String date,
                             @RequestParam("author") Artist author,
                             @RequestParam("imageFile") MultipartFile file)throws IOException {
        if(file.isEmpty()){
            return "error";
        }

        String fileName = file.getOriginalFilename() + "_" + System.currentTimeMillis();
        Path picturePath = PICTURE_PATH.resolve(fileName);
        file.transferTo(picturePath);

        Picture picture = new Picture(name, date, fileName, author);

        return "saved_picture";
    }

    @GetMapping("/{id}")
    public String getPost(Model model, @PathVariable long id, @PathVariable String ImageFilename) {
        Optional<Picture> picture = pictureService.findById(id);
        if (picture.isPresent()) {
            model.addAttribute("picture", picture.get());
            String likedText = userService.isPictureLiked(picture.get()) ? "Unlike" : "Like";

            model.addAttribute("likedText", likedText);
            model.addAttribute("imagePath", "/picture/" + picture.get().getImageFilename());

            return "show_picture";
        } else {
            return "picture_not_found";
        }
    }

    @GetMapping("/{ImageFilename}")
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

    @GetMapping("/{id}/edit")
    public String editPost(Model model, @PathVariable long id) {
        Optional<Picture> picture = pictureService.findById(id);
        if (picture.isPresent()) {
            model.addAttribute("picture", picture.get());
            return "show_picture";
        } else {
            return "picture_not_found";
        }
    }

    @PostMapping("/{id}/edit")
    public String updatePost(Model model, @PathVariable long id, Picture updatedPost) {
        Optional<Picture> picture = pictureService.findById(id);
        if (picture.isPresent()) {
            Picture oldPicture = picture.get();
            pictureService.update(oldPicture, updatedPost);
            return "redirect:/pictures/" + id;
        } else {
            return "post_not_found";
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
            return "redirect:/pictures/" + picId;
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
    public String likePost(@PathVariable Long picId, Long userId) {
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
