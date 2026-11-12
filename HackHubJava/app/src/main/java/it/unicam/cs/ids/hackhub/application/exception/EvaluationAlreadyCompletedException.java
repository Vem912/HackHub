package it.unicam.cs.ids.hackhub.application.exception;

public class EvaluationAlreadyCompletedException extends RuntimeException{
    public EvaluationAlreadyCompletedException() {
        super("Evaluation is already completed");
    }
}
