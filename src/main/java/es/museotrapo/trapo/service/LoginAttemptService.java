package es.museotrapo.trapo.security;

import org.springframework.stereotype.Service;

/**
 * Service class to manage login attempts and handle temporary account locking after multiple failed logins.
 * Implements logic to track login failures and enforce a "cooling-off" period if the user exceeds the maximum allowed attempts.
 */
@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3; // Maximum number of failed login attempts allowed
    private static final long LOCK_TIME_DURATION = 10 * 60 * 1000; // Lock duration in milliseconds (10 minutes in this case)

    private static int failedAttempts = 0; // Global counter to track the number of failed login attempts
    private static long lockTime = 0; // Timestamp representing when the system was locked (or when it will be unlocked)

    /**
     * Registers a failed login attempt.
     * If the maximum allowed attempts are exceeded, the system is locked for a duration.
     */
    public void registerLoginFailure() {
        // If the system is already locked, do not increment the failed attempts counter
        if (isBlocked()) return;

        // Increment the count of failed attempts
        failedAttempts++;

        // Check if the failed attempts exceed or meet the maximum allowed
        if (failedAttempts >= MAX_ATTEMPTS) {
            // Lock the system and set the lock time to the current time + lock duration
            lockTime = System.currentTimeMillis() + LOCK_TIME_DURATION;
        }
    }

    /**
     * Checks if the system (or user) is currently locked due to exceeding the maximum failed attempts.
     *
     * @return `true` if the system is currently locked, `false` otherwise.
     */
    public boolean isBlocked() {
        // If the lock time is 0, it means the system is not locked
        if (lockTime == 0) return false;

        // If the current time is past the lock expiration time, reset and unlock
        if (System.currentTimeMillis() > lockTime) {
            resetAttempts(); // Reset the failed attempts counter and unlock
            return false; // No longer blocked
        }

        // Otherwise, the system is locked
        return true;
    }

    /**
     * Resets the failed login attempts counter and unlocks the system.
     * Should be invoked after a successful login or when the lock duration has expired.
     */
    public void resetAttempts() {
        // Reset both the failed attempts counter and the lock time
        failedAttempts = 0;
        lockTime = 0;
    }

    /**
     * Returns the current number of failed login attempts.
     *
     * @return The number of failed attempts.
     */
    public int getFailedAttempts() {
        return failedAttempts;
    }
}