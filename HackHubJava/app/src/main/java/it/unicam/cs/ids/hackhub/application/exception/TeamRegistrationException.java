package it.unicam.cs.ids.hackhub.application.exception;

/** Base exception for a rejected team registration. */
public class TeamRegistrationException extends RuntimeException {
    public TeamRegistrationException(String message) {
        super(message);
    }
}
