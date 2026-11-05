package it.unicam.cs.ids.hackhub.application.exception;

public class HackathonNotFoundException extends TeamRegistrationException {
    public HackathonNotFoundException() {
        super("The hackathon was not found");
    }
}
