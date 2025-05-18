package es.museotrapo.trapo.service;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service class for sanitization operations.
 * Includes methods to sanitize input text (HTML content), filenames, and validate file extensions and content.
 */
@Service
public class SanitizeService {

    // Custom HTML sanitization policy to restrict what elements and attributes are allowed.
    private static final PolicyFactory CUSTOM_POLICY = new HtmlPolicyBuilder()
            // Allow specific HTML elements like headers and links.
            .allowElements("h1", "h2", "h3") // Allow header tags
            .allowElements("a") // Allow anchor (link) elements
            .allowUrlProtocols("https") // Restrict links to only allow HTTPS protocol
            // Allow `href` attribute only on links and add `rel="nofollow"` for security reasons.
            .allowAttributes("href").onElements("a")
            .requireRelNofollowOnLinks()
            .toFactory();

    // HTML sanitization policy that allows basic formatting (bold, italic, etc.), links,
    // and additionally includes the custom elements from the CUSTOM_POLICY defined above.
    private static final PolicyFactory POLICY = Sanitizers.FORMATTING
            .and(Sanitizers.LINKS)
            .and(CUSTOM_POLICY);

    // Array of allowed file extensions, used for file validation.
    private static final String[] ALLOWED_EXTENSIONS = {"pdf"};

    /**
     * Sanitizes an input string by applying the defined HTML sanitation policy.
     * Removes or restricts unsafe HTML content.
     *
     * @param input the input string containing potential HTML content.
     * @return a sanitized version of the input string with only allowed HTML elements and attributes.
     */
    public static String sanitize(String input) {
        return POLICY.sanitize(input);
    }

    /**
     * Sanitizes a filename to ensure it is safe for file storage.
     *
     * - Replaces invalid or potentially dangerous characters with underscores.
     * - Limits the length of the filename to avoid filesystem errors.
     *
     * @param filename the original filename provided by the user.
     * @return a sanitized and safe version of the filename.
     * @throws IllegalArgumentException if the provided filename is null or empty.
     */
    public String sanitizeFileName(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("The file does not have a legit name");
        }

        // Extract only the base portion of the filename to avoid directory traversal issues.
        String sanitizedName = Paths.get(filename).toString();

        // Replace invalid characters with underscores to ensure the filename contains only safe characters.
        sanitizedName = sanitizedName.replaceAll("[^a-zA-Z0-9.\\-]", "_");

        // Limit the filename length to 120 characters for safety and filesystem compatibility.
        if (sanitizedName.length() > 120) {
            sanitizedName = sanitizedName.substring(0, 120); // Truncate to 120 characters.
        }
        return sanitizedName;
    }

    /**
     * Validates the file extension and content type of an uploaded file.
     *
     * - Checks if the file has a valid extension (only PDFs are allowed).
     * - Verifies the file's content type matches the expected type for PDFs.
     *
     * @param fileName    the name of the uploaded file.
     * @param fileContent the content of the uploaded file as an InputStream.
     * @throws IllegalArgumentException if the file extension or content type is invalid.
     * @throws IOException              if an error occurs while accessing file information.
     */
    public void validateFileExtensionAndContent(String fileName, InputStream fileContent) throws IOException {
        // Extract the file extension and convert it to lowercase for case-insensitive validation.
        String extension = fileName
                .substring(fileName.lastIndexOf('.') + 1)
                .toLowerCase();

        // Ensure the file extension is a valid and allowed format (in this case, only "pdf").
        if (!"pdf".equals(extension)) {
            throw new IllegalArgumentException("Invalid file extension: only PDF files are allowed.");
        }

        // Probe the MIME type of the file to verify its content type matches the expected type.
        String mimeType = Files.probeContentType(Paths.get(fileName));
        if (!"application/pdf".equals(mimeType)) {
            throw new IllegalArgumentException("Invalid content type: the uploaded file is not a valid PDF.");
        }
    }
}