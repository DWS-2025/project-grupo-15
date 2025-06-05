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
        // Reject null or empty file names immediately
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("The file does not have a legit name");
        }

        // Reject any attempt at directory traversal or path injection
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\\\")) {
            throw new IllegalArgumentException("Invalid file name: path traversal or directory injection attempt");
        }

        // Extract only the file name (removes any path fragments if present)
        String baseName = Paths.get(filename).getFileName().toString();

        // Replace any characters that are not safe (allow only letters, numbers, dots, dashes, underscores)
        String sanitizedName = baseName.replaceAll("[^a-zA-Z0-9._-]", "_");

        // Limit the file name to a maximum of 120 characters to avoid abuse
        if (sanitizedName.length() > 120) {
            sanitizedName = sanitizedName.substring(0, 120);
        }

        return sanitizedName;
    }


    /*
     * Validate the file extension and content type.
     * Only allows PDF files.
     */
    public void validateFileExtensionAndContent(String fileName, InputStream fileContent) throws IOException {
        // Validate that the file name is not null and matches a strict pattern for .pdf files
        if (fileName == null || !fileName.matches("^[\\w\\s-]+\\.pdf$") || fileName.chars().filter(ch -> ch == '.').count() != 1) {
            throw new IllegalArgumentException("Invalid file name or extension: only clean .pdf files are allowed.");
        }

        // Read the first 4 bytes to check the PDF magic number ("%PDF")
        byte[] header = new byte[4];
        if (fileContent.read(header) != 4) {
            throw new IllegalArgumentException("File is too short or unreadable.");
        }

        String signature = new String(header);
        if (!"%PDF".equals(signature)) {
            throw new IllegalArgumentException("Invalid file content: not a real PDF.");
        }
    }
}
