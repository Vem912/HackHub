package it.unicam.cs.ids.hackhub.application.exception;

/** The submission deadline has passed. */
public class SubmissionDeadlinePassedException extends TeamRegistrationException {
    public SubmissionDeadlinePassedException() {
        super("The submission deadline has passed.");
    }
}
