package it.unicam.cs.ids.hackhub.application.exception;

/** The requested submission does not exist. */
public class SubmissionNotFoundException extends RuntimeException {
    public SubmissionNotFoundException() {
        super("Submission not found.");
    }
}
