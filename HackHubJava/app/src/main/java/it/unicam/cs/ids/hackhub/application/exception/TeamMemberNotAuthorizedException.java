package it.unicam.cs.ids.hackhub.application.exception;

/** The requesting user is not a member of the team. */
public class TeamMemberNotAuthorizedException extends TeamRegistrationException {
    public TeamMemberNotAuthorizedException() {
        super("The user is not a member of the team.");
    }
}
