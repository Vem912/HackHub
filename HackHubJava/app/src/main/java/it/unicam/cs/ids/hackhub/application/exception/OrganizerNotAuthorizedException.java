package it.unicam.cs.ids.hackhub.application.exception;

/** The user is not the organizer of the requested hackathon. */
public class OrganizerNotAuthorizedException extends RuntimeException {
    public OrganizerNotAuthorizedException() {
        super("The user is not authorized to access this hackathon.");
    }
}
