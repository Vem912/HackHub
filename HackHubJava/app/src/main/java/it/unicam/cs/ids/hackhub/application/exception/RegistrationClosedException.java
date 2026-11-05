package it.unicam.cs.ids.hackhub.application.exception;

public class RegistrationClosedException extends TeamRegistrationException {
    public RegistrationClosedException() {
        super("Team registration is not open for this hackathon");
    }
}
