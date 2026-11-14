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

    @GetMapping("/listHackathonSubmissions")
    public List<SubmissionResponse> listHackathonSubmissions(@PathVariable UUID hackathonId) {
        return listUseCase.list(hackathonId).stream()
                .map(result -> new SubmissionResponse(
                        result.submissionId(),
                        result.teamId(),
                        result.title(),
                        result.description(),
                        result.fileName(),
                        result.submittedAt()))
                .toList();
    }
}
