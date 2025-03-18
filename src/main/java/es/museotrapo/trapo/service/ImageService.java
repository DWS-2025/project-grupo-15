package es.museotrapo.trapo.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service class for handling image storage, retrieval, and deletion.
 * Stores images locally in the "pictures" directory.
 */
@Service
public class ImageService {
    public Blob localImageToBlob(String localFilePath){
        File imageFile = new File(localFilePath);
        if (imageFile.exists()) {
            System.out.println("image file found");
            try {
                return BlobProxy.generateProxy(imageFile.toURI().toURL().openStream(), imageFile.length());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error at processing the image");
            }
        }
        System.out.println("image file not found");

        return null;
    }
}