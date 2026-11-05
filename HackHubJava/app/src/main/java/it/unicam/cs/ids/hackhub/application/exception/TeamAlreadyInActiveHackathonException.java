package it.unicam.cs.ids.hackhub.application.exception;

public class TeamAlreadyInActiveHackathonException extends TeamRegistrationException {
    public TeamAlreadyInActiveHackathonException() {
        super("The team is already registered for an active hackathon");
    }
}
