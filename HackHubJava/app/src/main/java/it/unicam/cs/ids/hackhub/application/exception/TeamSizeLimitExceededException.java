package it.unicam.cs.ids.hackhub.application.exception;

public class TeamSizeLimitExceededException extends TeamRegistrationException {
    public TeamSizeLimitExceededException() {
        super("The team exceeds the hackathon team size limit");
    }
}
