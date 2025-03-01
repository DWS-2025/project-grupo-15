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

/**
 * Service class for handling image storage, retrieval, and deletion.
 * Stores images locally in the "pictures" directory.
 */
@Service
public class ImageService {

    // Define the directory where images will be stored
    private static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"), "pictures");

    /**
     * Saves an uploaded image file locally and returns its generated name.
     *
     * @param multiPartFile The image file uploaded by the user.
     * @return The generated unique file name.
     * @throws IOException If an error occurs while saving the file.
     */
    public String createImage(MultipartFile multiPartFile) throws IOException {
        // Check if the file is empty
        if (multiPartFile.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Generate a unique name for the image using the current timestamp
        String originalName = System.currentTimeMillis() + "_" + multiPartFile.getOriginalFilename();

        // Validate the file extension to ensure it's an image
        if (!originalName.matches("(?i).*\\.(jpg|jpeg|gif|png)")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The URL is not an image resource");
        }

        Files.createDirectories(IMAGES_FOLDER);// Ensure the images directory exists
        Path imagePath = IMAGES_FOLDER.resolve(originalName);// Resolve the full image path

        try {
            // Save the uploaded file to the designated folder
            multiPartFile.transferTo(imagePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't save image locally", ex);
        }

        return originalName;
    }

    /**
     * Retrieves an image as a Resource from local storage.
     *
     * @param imageName The name of the image file to retrieve.
     * @return The image as a Resource.
     */
    public Resource getImage(String imageName) {
        Path imagePath = IMAGES_FOLDER.resolve(imageName);
        try {
            return new UrlResource(imagePath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't get local image");
        }
    }

    /**
     * Deletes an image from local storage based on its URL.
     *
     * @param imageUrl The full URL of the image to delete.
     */
    public void deleteImage(String imageUrl) {
        // Extract the image file name from the provided URL
        String[] tokens = imageUrl.split("/");
        String imageName = tokens[tokens.length - 1];

        try {
            // Attempt to delete the image file
            IMAGES_FOLDER.resolve(imageName).toFile().delete();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't delete local image");
        }
    }
}