package it.unicam.cs.ids.hackhub.application.exception;

public class TeamNotFoundException extends TeamRegistrationException {
    public TeamNotFoundException() {
        super("The team was not found");
    }
}
