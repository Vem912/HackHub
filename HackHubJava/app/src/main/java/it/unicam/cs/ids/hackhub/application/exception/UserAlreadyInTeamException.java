package it.unicam.cs.ids.hackhub.application.exception;

/** Indicates that a user cannot create a team because they already belong to one. */
public class UserAlreadyInTeamException extends RuntimeException {
    public UserAlreadyInTeamException() {
        super("The user already belongs to a team");
    }
}
