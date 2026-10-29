package it.unicam.cs.ids.hackhub.presentation.controller;

import it.unicam.cs.ids.hackhub.application.dto.command.CreateHackathonCommand;
import it.unicam.cs.ids.hackhub.application.port.in.CreateHackathonUseCase;
import it.unicam.cs.ids.hackhub.presentation.dto.request.CreateHackathonRequest;
import it.unicam.cs.ids.hackhub.presentation.dto.response.CreateHackathonResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    public HackathonController(CreateHackathonUseCase createHackathonUseCase) {
        this.createHackathonUseCase = Objects.requireNonNull(createHackathonUseCase);
    }

    /** Creates a hackathon and returns its representation with HTTP 201. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateHackathonResponse createHackathon(@Valid @RequestBody CreateHackathonRequest request) {
        var command = new CreateHackathonCommand(
                request.name(),
                request.location(),
                request.prize(),
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
}
