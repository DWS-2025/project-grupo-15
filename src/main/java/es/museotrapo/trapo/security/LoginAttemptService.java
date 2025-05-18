package es.museotrapo.trapo.security;

import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3; // Maximum number of failed attempts
    private static final long LOCK_TIME_DURATION = 10 * 60 * 1000; // Block time (15 seconds in ms)

    private static int failedAttempts = 0; // Global counter of failed attempts
    private static long lockTime = 0; // Time when the user is blocked

    // Register failed login attempt
    public void registerLoginFailure() {
        // if is blocked, do not permit any more attempts
        if (isBlocked()) return;

        failedAttempts++; // Increment counter of failed attempts

        // Block if maximum attempts reached
        if (failedAttempts >= MAX_ATTEMPTS) {
            lockTime = System.currentTimeMillis() + LOCK_TIME_DURATION;
        }
    }

    /*
     * Check if the user is blocked.
     */
    public boolean isBlocked() {
        if (lockTime == 0) return false; 
        if (System.currentTimeMillis() > lockTime) { 
            resetAttempts(); 
            return false;
        }
        return true; 
    }

    // Reset the failed attempts and lock time
    public void resetAttempts() {
        failedAttempts = 0;
        lockTime = 0;
    }

    // Get the number of failed attempts
    public int getFailedAttempts() {
        return failedAttempts;
    }
}