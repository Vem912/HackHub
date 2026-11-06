package it.unicam.cs.ids.hackhub.presentation.controller;

import it.unicam.cs.ids.hackhub.application.port.in.ListHackathonSubmissionsUseCase;
import it.unicam.cs.ids.hackhub.presentation.dto.response.SubmissionResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/hackathons/{hackathonId}/submissions")
public class HackathonSubmissionController {
    private final ListHackathonSubmissionsUseCase listUseCase;

    public HackathonSubmissionController(ListHackathonSubmissionsUseCase listUseCase) {
        this.listUseCase = Objects.requireNonNull(listUseCase);
    }

    @GetMapping
    public List<SubmissionResponse> list(@PathVariable String hackathonId) {
        return listUseCase.list(UUID.fromString(hackathonId)).stream()
                .map(result -> new SubmissionResponse(
                        result.submissionId().toString(),
                        result.teamId().toString(),
                        result.title(),
                        result.description(),
                        result.fileName(),
                        result.fileContent(),
                        result.submittedAt()))
                .toList();
    }
}
