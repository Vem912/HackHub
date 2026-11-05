package it.unicam.cs.ids.hackhub.application.exception;

/** The uploaded file is not a valid submission archive. */
public class InvalidSubmissionFileException extends IllegalArgumentException {
    public InvalidSubmissionFileException(String message) {
        super(message);
    }
}
