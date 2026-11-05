package it.unicam.cs.ids.hackhub.application.exception;

public class TeamAlreadyRegisteredException extends TeamRegistrationException {
    public TeamAlreadyRegisteredException() {
        super("The team is already registered for this hackathon");
    }
}
