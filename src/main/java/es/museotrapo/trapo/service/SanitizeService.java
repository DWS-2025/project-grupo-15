package es.museotrapo.trapo.service;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class SanitizeService {

    private static final PolicyFactory POLICY = Sanitizers.FORMATTING.and(Sanitizers.LINKS); // Permitir formato básico y enlaces

    // Método para sanitizar entradas de texto con HTML
    public static String sanitize(String input) {
        return POLICY.sanitize(input);
    }
}