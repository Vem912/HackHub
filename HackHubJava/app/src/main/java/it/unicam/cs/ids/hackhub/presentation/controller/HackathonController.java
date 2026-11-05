package it.unicam.cs.ids.hackhub.presentation.controller;

import it.unicam.cs.ids.hackhub.application.dto.command.CreateHackathonCommand;
import it.unicam.cs.ids.hackhub.application.dto.command.RegisterTeamToHackathonCommand;
import it.unicam.cs.ids.hackhub.application.port.in.CreateHackathonUseCase;
import it.unicam.cs.ids.hackhub.application.port.in.RegisterTeamToHackathonUseCase;
import it.unicam.cs.ids.hackhub.presentation.dto.request.CreateHackathonRequest;
import it.unicam.cs.ids.hackhub.presentation.dto.request.RegisterTeamToHackathonRequest;
import it.unicam.cs.ids.hackhub.presentation.dto.response.CreateHackathonResponse;
import it.unicam.cs.ids.hackhub.presentation.dto.response.RegisterTeamToHackathonResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

/** HTTP adapter that maps hackathon requests and responses to the application use case. */
@RestController
@RequestMapping("/hackathons")
public class HackathonController {

    private final CreateHackathonUseCase createHackathonUseCase;
    private final RegisterTeamToHackathonUseCase registerTeamToHackathonUseCase;

    public HackathonController(
            CreateHackathonUseCase createHackathonUseCase,
            RegisterTeamToHackathonUseCase registerTeamToHackathonUseCase) {
        this.createHackathonUseCase = Objects.requireNonNull(createHackathonUseCase);
        this.registerTeamToHackathonUseCase = Objects.requireNonNull(registerTeamToHackathonUseCase);
    }

    /** Creates a hackathon and returns its representation with HTTP 201. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateHackathonResponse createHackathon(@Valid @RequestBody CreateHackathonRequest request) {
        var command = new CreateHackathonCommand(
                request.name(),
                request.location(),
                request.prize(),
                request.maxTeamMembers(),
                UUID.fromString(request.organizerId()),
                UUID.fromString(request.judgeId()),
                request.mentorIds().stream().map(UUID::fromString).toList(),
                request.teamRegistrationDeadline(),
                request.hackathonStart(),
                request.hackathonEnd());
        var result = createHackathonUseCase.create(command);
        return new CreateHackathonResponse(
                result.id().toString(),
                result.name());
    }

    @PostMapping("/{hackathonId}/teams")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterTeamToHackathonResponse registerTeam(
            @Valid @RequestBody RegisterTeamToHackathonRequest request) {
        var result = registerTeamToHackathonUseCase.register(
                new RegisterTeamToHackathonCommand(
                        UUID.fromString(request.userId()),
                        UUID.fromString(request.teamId()),
                        UUID.fromString(request.hackathonId())));
        return new RegisterTeamToHackathonResponse(result.teamId(), result.hackathonId());
    }
}
