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
@Service // Spring annotation indicating this is a service class
public class ImageService {

    /**
     * Converts a local image file to a Blob object.
     * This Blob can be used for storage or retrieval from a database.
     *
     * @param localFilePath the file path of the local image
     * @return a Blob representation of the image
     * @throws ResponseStatusException if there is an error processing the image
     */
    public Blob localImageToBlob(String localFilePath) {
        // Creates a File object from the provided file path
        File imageFile = new File(localFilePath);

        // Checks if the file exists
        if (imageFile.exists()) {
            try {
                // Generates a Blob from the image file's URI and content length
                return BlobProxy.generateProxy(imageFile.toURI().toURL().openStream(), imageFile.length());
            } catch (IOException e) {
                // Throws a custom exception if an error occurs during file processing
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error at processing the image");
            }
        }

        // Prints a message if the image file is not found
        System.out.println("image file not found");

        return null; // Returns null if the file is not found
    }
}
