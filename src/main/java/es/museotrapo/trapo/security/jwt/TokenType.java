package es.museotrapo.trapo.security.jwt;

import java.time.Duration;

public enum TokenType {
    ACCESS(Duration.ofMinutes(5L), "AuthToken"),
    REFRESH(Duration.ofDays(7L), "RefreshToken");

    public final Duration duration;
    public final String cookieName;

    private TokenType(Duration duration, String cookieName) {
        this.duration = duration;
        this.cookieName = cookieName;
    }
}
