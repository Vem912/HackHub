package it.unicam.cs.ids.hackhub.presentation.controller;

import it.unicam.cs.ids.hackhub.application.dto.command.SubmitTeamSubmissionCommand;
import it.unicam.cs.ids.hackhub.application.exception.InvalidSubmissionFileException;
import it.unicam.cs.ids.hackhub.application.port.in.SubmitTeamSubmissionUseCase;
import it.unicam.cs.ids.hackhub.domain.valueobject.SubmissionFile;
import it.unicam.cs.ids.hackhub.presentation.dto.request.SubmitTeamSubmissionRequest;
import it.unicam.cs.ids.hackhub.presentation.dto.response.SubmitTeamSubmissionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/** HTTP adapter for creating or replacing the current team submission. */
@RestController
@RequestMapping("/hackathons/{hackathonId}/teams/{teamId}/submission")
public class SubmissionController {
    private final SubmitTeamSubmissionUseCase submitUseCase;

    public SubmissionController(
            SubmitTeamSubmissionUseCase submitUseCase
    ) {
        this.submitUseCase = Objects.requireNonNull(submitUseCase);
    }

    @PutMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public SubmitTeamSubmissionResponse submit(
            @Valid @ModelAttribute SubmitTeamSubmissionRequest request
    ) {
        try {
            var result = submitUseCase.submit(new SubmitTeamSubmissionCommand(
                    UUID.fromString(request.userId()),
                    UUID.fromString(request.teamId()),
                    UUID.fromString(request.hackathonId()),
                    request.title(),
                    request.description(),
                    new SubmissionFile(
                            request.file().getOriginalFilename(),
                            request.file().getBytes())));
            return new SubmitTeamSubmissionResponse(
                    result.submissionId().toString(),
                    result.teamId().toString(),
                    result.hackathonId().toString(),
                    result.fileName(),
                    result.submittedAt(),
                    result.updated());
        } catch (IOException exception) {
            throw new InvalidSubmissionFileException("The uploaded file could not be read.");
        }
    }
}
