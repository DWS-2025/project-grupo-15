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

@Service
public class SanitizeService {

    private static final PolicyFactory CUSTOM_POLICY = new HtmlPolicyBuilder()
            .allowElements("h1", "h2", "h3") // Permit headings
            .allowElements("a") // Permit links
            .allowUrlProtocols("https") // Permit only HTTPS links
            .allowAttributes("href").onElements("a") // Permit href attribute on links
            .requireRelNofollowOnLinks() // Require rel="nofollow" on links
            .toFactory();

    private static final PolicyFactory POLICY = Sanitizers.FORMATTING.and(Sanitizers.LINKS).and(CUSTOM_POLICY); // Permit formatting and links

    private static final String[] ALLOWED_EXTENSIONS = {"pdf"};

    
    // Method to sanitize text inputs with HTML
    public static String sanitize(String input) {
        return POLICY.sanitize(input);
    }

    /*
     * Sanitize the file name to prevent directory traversal attacks and
     */
    public String sanitizeFileName(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("The file does not have a legit name");
        }
        // Extract only the base name of the file (prevents relative or absolute paths).
        String sanitizedName = Paths.get(filename).toString();

        // Replace invalid or dangerous characters with underscores.
        // Only allows letters, numbers, dashes, and dots.
        sanitizedName = sanitizedName.replaceAll("[^a-zA-Z0-9.\\-]", "_");

        // Limit the name to a maximum of 120 characters.
        if (sanitizedName.length() > 120) {
            sanitizedName = sanitizedName.substring(120);
        }
        return sanitizedName;
    }

    /*
     * Validate the file extension and content type.
     * Only allows PDF files.
     */
    public void validateFileExtensionAndContent(String fileName, InputStream fileContent) throws IOException {
        String extension = fileName
                .substring(fileName.lastIndexOf('.') + 1)
                .toLowerCase();
        if (!"pdf".equals(extension)) {
            throw new IllegalArgumentException("Invalid file extension: only PDF files are allowed.");
        }

        String mimeType = Files
                .probeContentType(Paths.get(fileName));
        if (!"application/pdf".equals(mimeType)) {
            throw new IllegalArgumentException("Invalid content type: the uploaded file is not a valid PDF.");
        }

    }
}
