package it.unicam.cs.ids.hackhub.presentation.controller;

import it.unicam.cs.ids.hackhub.application.exception.UserAlreadyInTeamException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Converts invalid domain or request arguments into HTTP 400 problem responses. */
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleInvalidRequest(IllegalArgumentException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UserAlreadyInTeamException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleUserAlreadyInTeam(UserAlreadyInTeamException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }
}
