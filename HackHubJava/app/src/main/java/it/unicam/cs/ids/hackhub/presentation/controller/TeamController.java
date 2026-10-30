package it.unicam.cs.ids.hackhub.presentation.controller;

import it.unicam.cs.ids.hackhub.application.dto.command.CreateTeamCommand;
import it.unicam.cs.ids.hackhub.application.port.in.CreateTeamUseCase;
import it.unicam.cs.ids.hackhub.presentation.dto.request.CreateTeamRequest;
import it.unicam.cs.ids.hackhub.presentation.dto.response.CreateTeamResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

/** HTTP adapter that maps team requests and responses to the application use case. */
@RestController
@RequestMapping("/teams")
public class TeamController {
    private final CreateTeamUseCase createTeamUseCase;

    public TeamController(CreateTeamUseCase createTeamUseCase) {
        this.createTeamUseCase = Objects.requireNonNull(createTeamUseCase);
    }

    /** Creates a team and returns its representation with HTTP 201. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateTeamResponse createTeam(@Valid @RequestBody CreateTeamRequest request) {
        var command = new CreateTeamCommand(
                request.name(),
                UUID.fromString(request.creatorId()),
                request.paymentDetails());
        var result = createTeamUseCase.create(command);
        return new CreateTeamResponse(result.id().toString(), result.name());
    }
}
