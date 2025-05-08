package es.museotrapo.trapo.security.jwt;


public class AuthResponse {
    private Status status;
    private String message;
    private String error;
    public enum Status {
        SUCCESS, FAILURE
    }

    public AuthResponse() {
    }

    public AuthResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public AuthResponse(Status status, String message, String error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String toString() {
        String var10000 = String.valueOf(this.status);
        return "LoginResponse [status=" + var10000 + ", message=" + this.message + ", error=" + this.error + "]";
    }
}
