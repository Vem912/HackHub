package it.unicam.cs.ids.hackhub.presentation.controller;

import it.unicam.cs.ids.hackhub.application.exception.UserAlreadyInTeamException;
import it.unicam.cs.ids.hackhub.application.exception.HackathonNotFoundException;
import it.unicam.cs.ids.hackhub.application.exception.TeamNotFoundException;
import it.unicam.cs.ids.hackhub.application.exception.TeamMemberNotAuthorizedException;
import it.unicam.cs.ids.hackhub.application.exception.TeamRegistrationException;
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

    @ExceptionHandler(TeamMemberNotAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ProblemDetail handleUnauthorizedTeamMember(TeamMemberNotAuthorizedException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler({HackathonNotFoundException.class, TeamNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleRegistrationTargetNotFound(TeamRegistrationException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(TeamRegistrationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleTeamRegistration(TeamRegistrationException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }
}
