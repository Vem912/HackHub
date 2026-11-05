package it.unicam.cs.ids.hackhub.application.exception;

/** The hackathon is not accepting submissions yet or anymore. */
public class HackathonNotInProgressException extends TeamRegistrationException {
    public HackathonNotInProgressException() {
        super("The hackathon is not in progress.");
    }
}
