package it.unicam.cs.ids.hackhub.application.exception;

public class TeamMemberIsStaffException extends TeamRegistrationException {
    public TeamMemberIsStaffException() {
        super("A team member belongs to the hackathon staff");
    }
}
