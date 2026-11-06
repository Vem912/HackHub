package it.unicam.cs.ids.hackhub.application.exception;

/** The user is not assigned as a judge for the hackathon. */
public class JudgeNotAuthorizedException extends RuntimeException {
    public JudgeNotAuthorizedException() {
        super("The user is not authorized to judge this hackathon.");
    }
}
