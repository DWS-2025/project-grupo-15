package es.museotrapo.trapo.security;

import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3; // Máximo de intentos fallidos
    private static final long LOCK_TIME_DURATION = 10 * 60 * 1000; // Tiempo de bloqueo (15 segundos en ms)

    private static int failedAttempts = 0; // Contador global de intentos fallidos
    private static long lockTime = 0; // Momento en que el sistema está bloqueado

    // Registrar un intento fallido
    public void registerLoginFailure() {
        // Si está bloqueado, no permitir más intentos
        if (isBlocked()) return;

        failedAttempts++; // Incrementar intentos fallidos

        // Bloquear si supera el límite
        if (failedAttempts >= MAX_ATTEMPTS) {
            lockTime = System.currentTimeMillis() + LOCK_TIME_DURATION;
        }
    }

    // Verificar si está bloqueado
    public boolean isBlocked() {
        if (lockTime == 0) return false; // No bloqueado
        if (System.currentTimeMillis() > lockTime) { // Bloqueo expirado
            resetAttempts(); // Reinicia intentos y desbloquea
            return false;
        }
        return true; // Sigue bloqueado
    }

    // Resetear el contador de intentos y desbloquear
    public void resetAttempts() {
        failedAttempts = 0;
        lockTime = 0;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }
}