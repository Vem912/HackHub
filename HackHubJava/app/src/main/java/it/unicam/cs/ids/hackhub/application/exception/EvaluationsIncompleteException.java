package it.unicam.cs.ids.hackhub.application.exception;

/** At least one submission has not been evaluated yet. */
public class EvaluationsIncompleteException extends RuntimeException {
    public EvaluationsIncompleteException(String message) {
        super(message);
    }
}
