package es.museotrapo.trapo.service;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.Normalizer;
import java.util.Locale;

/**
 * Service for sanitizing HTML inputs, file names, and validating uploaded PDF files.
 */
@Service
public class SanitizeService {

    /**
     * Custom HTML policy allowing specific elements and enforcing nofollow on links.
     */
    private static final PolicyFactory CUSTOM_POLICY = new HtmlPolicyBuilder()
            .allowElements("h1", "h2", "h3")   // Allow heading elements
            .allowElements("a")                    // Allow anchor elements
            .allowUrlProtocols("https")            // Restrict links to HTTPS
            .allowAttributes("href").onElements("a") // Allow href attribute on <a>
            .requireRelNofollowOnLinks()            // Automatically add rel="nofollow"
            .toFactory();

    /**
     * Combined policy permitting basic formatting, links, and custom rules.
     */
    private static final PolicyFactory POLICY = Sanitizers.FORMATTING
            .and(Sanitizers.LINKS)
            .and(CUSTOM_POLICY);

    /**
     * Allowed file extensions for uploads.
     */
    private static final String[] ALLOWED_EXTENSIONS = {"pdf"};

    /**
     * Sanitize input HTML according to the defined policy.
     * @param input raw HTML string
     * @return sanitized HTML
     */
    public static String sanitize(String input) {
        return POLICY.sanitize(input);
    }

    /**
     * Clean and validate a file name to prevent path traversal or invalid characters.
     * @param filename original uploaded file name
     * @return sanitized file name (max 120 chars, only alphanumeric, dot, dash, underscore)
     */
    public String sanitizeFileName(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid file name");
        }
        // Normalize Unicode to NFC form
        String normalized = Normalizer.normalize(filename.trim(), Normalizer.Form.NFC);
        // Extract only the file name part
        Path path = Paths.get(normalized).getFileName();
        String baseName = path == null ? "" : path.toString();
        // Prevent directory traversal sequences
        if (baseName.contains("..") || baseName.contains("/") || baseName.contains("\\")) {
            throw new IllegalArgumentException("Invalid file name: path traversal attempt");
        }
        // Replace disallowed chars with underscore
        String sanitized = baseName.replaceAll("[^a-zA-Z0-9._-]", "_");
        // Enforce maximum length
        return sanitized.length() > 120 ? sanitized.substring(0, 120) : sanitized;
    }

    /**
     * Validate that the uploaded file is a real PDF and matches expected MIME type.
     * @param fileName original file name for extension check
     * @param fileContent input stream of the uploaded file
     * @throws IOException on IO error or invalid file
     */
    public void validateFileExtensionAndContent(String fileName, InputStream fileContent) throws IOException {
        // Basic file name presence check
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid file name");
        }
        // Normalize and extract base name
        String normalized = Normalizer.normalize(fileName.trim(), Normalizer.Form.NFC);
        String baseName = Paths.get(normalized).getFileName().toString();
        String lower = baseName.toLowerCase(Locale.ROOT);
        // Ensure exactly one .pdf extension
        if (!lower.matches("^[\\w\\s-]+\\.pdf$") || lower.chars().filter(ch -> ch == '.').count() != 1) {
            throw new IllegalArgumentException("Only .pdf extension allowed");
        }

        // Wrap stream to support mark/reset
        if (!fileContent.markSupported()) {
            fileContent = new BufferedInputStream(fileContent);
        }
        // Mark stream up to expected max size (e.g., 6MB)
        fileContent.mark(5 * 1024 * 1024);

        // Read and verify the PDF magic header (%PDF)
        byte[] header = new byte[4];
        if (fileContent.read(header) != 4) {
            throw new IllegalArgumentException("File too short or unreadable");
        }
        String sig = new String(header, StandardCharsets.US_ASCII);
        if (!"%PDF".equals(sig)) {
            throw new IllegalArgumentException("Invalid PDF file");
        }
        // Reset to start for full inspection
        fileContent.reset();

        // Write stream to a temporary file for MIME type probing
        Path temp = Files.createTempFile("upload-", ".pdf");
        try (OutputStream os = Files.newOutputStream(temp, StandardOpenOption.WRITE)) {
            byte[] buf = new byte[8192];
            int read;
            while ((read = fileContent.read(buf)) != -1) {
                os.write(buf, 0, read);
            }
        }
        // Probe the content type of the temp file
        String type = Files.probeContentType(temp);
        // Clean up temp file
        Files.deleteIfExists(temp);
        // Reject if not standard PDF MIME type
        if (!"application/pdf".equals(type)) {
            throw new IllegalArgumentException("Unexpected MIME type: " + type);
        }

        // Reset stream again for downstream processing
        fileContent.reset();
    }
}
