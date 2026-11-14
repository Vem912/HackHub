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
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/createHackathon")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateHackathonResponse createHackathon(@Valid @RequestBody CreateHackathonRequest request) {
        var command = new CreateHackathonCommand(
                request.name(),
                request.location(),
                request.prize(),
                request.maxTeamMembers(),
                request.organizerId(),
                request.judgeId(),
                request.mentorIds(),
                request.teamRegistrationDeadline(),
                request.hackathonStart(),
                request.hackathonEnd());
        var result = createHackathonUseCase.create(command);
        return new CreateHackathonResponse(result.id(), result.name());
    }

    @PostMapping("/{hackathonId}/registerTeamToHackathon")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterTeamToHackathonResponse registerTeamToHackathon(
            @PathVariable UUID hackathonId,
            @Valid @RequestBody RegisterTeamToHackathonRequest request) {
        var result = registerTeamToHackathonUseCase.register(
                new RegisterTeamToHackathonCommand(
                        request.userId(),
                        request.teamId(),
                        hackathonId));
        return new RegisterTeamToHackathonResponse(result.teamId(), result.hackathonId());
    }
}
