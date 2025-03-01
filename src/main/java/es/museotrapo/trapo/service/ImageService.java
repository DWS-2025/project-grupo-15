package es.museotrapo.trapo.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ImageService {

    private static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"), "pictures");

    public String createImage(MultipartFile multiPartFile) throws IOException {

        if(multiPartFile.isEmpty()){
            throw new IllegalArgumentException("File is empty");
        }

        String originalName = System.currentTimeMillis() + "_" + multiPartFile.getOriginalFilename();

        if(!originalName.matches("(?i).*\\.(jpg|jpeg|gif|png)")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The url is not an image resource");  
        }

        Files.createDirectories(IMAGES_FOLDER);

        Path imagePath = IMAGES_FOLDER.resolve(originalName);

        try {
            multiPartFile.transferTo(imagePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't save image locally", ex);
        } 

        return originalName;
    }

    public Resource getImage(String imageName) {
        Path imagePath = IMAGES_FOLDER.resolve(imageName);
        try {
            return new UrlResource(imagePath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't get local image");
        }
    }

    public void deleteImage(String image_url) {
        String[] tokens = image_url.split("/");
        String image_name = tokens[tokens.length -1 ];

        try {
            IMAGES_FOLDER.resolve(image_name).toFile().delete();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't delete local image");
        }
    }
}
