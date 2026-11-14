package it.unicam.cs.ids.hackhub.presentation.controller;

import it.unicam.cs.ids.hackhub.application.dto.command.CompleteHackathonEvaluationCommand;
import it.unicam.cs.ids.hackhub.application.dto.command.ListHackathonEvaluationsCommand;
import it.unicam.cs.ids.hackhub.application.port.in.CompleteHackathonEvaluationUseCase;
import it.unicam.cs.ids.hackhub.application.port.in.ListHackathonEvaluationsUseCase;
import it.unicam.cs.ids.hackhub.presentation.dto.request.CompleteHackathonEvaluationRequest;
import it.unicam.cs.ids.hackhub.presentation.dto.response.EvaluationResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

/** HTTP adapter for completing an hackathon evaluation phase. */
@RestController
@RequestMapping("/hackathons/{hackathonId}/evaluation")
public class HackathonEvaluationController {
    private final CompleteHackathonEvaluationUseCase completeEvaluationUseCase;
    private final ListHackathonEvaluationsUseCase listEvaluationsUseCase;

    public HackathonEvaluationController(
            CompleteHackathonEvaluationUseCase completeEvaluationUseCase,
            ListHackathonEvaluationsUseCase listEvaluationsUseCase) {
        this.completeEvaluationUseCase = Objects.requireNonNull(completeEvaluationUseCase);
        this.listEvaluationsUseCase = Objects.requireNonNull(listEvaluationsUseCase);
    }

    @GetMapping("/listHackathonEvaluation")
    public List<EvaluationResponse> listHackathonEvaluation(
            @PathVariable UUID hackathonId,
            @RequestParam UUID organizerId) {
        return listEvaluationsUseCase.list(new ListHackathonEvaluationsCommand(
                        hackathonId, organizerId))
                .stream()
                .map(result -> new EvaluationResponse(
                        result.evaluationId(),
                        result.submissionId(),
                        result.judgeId(),
                        result.score(),
                        result.judgment()))
                .toList();
    }

    @PostMapping("/completeHackathonEvaluation")
    @ResponseStatus(HttpStatus.OK)
    public void completeHackathonEvaluation(
            @PathVariable UUID hackathonId,
            @Valid @RequestBody CompleteHackathonEvaluationRequest request) {
        completeEvaluationUseCase.completeEvaluation(
                new CompleteHackathonEvaluationCommand(
                        hackathonId,
                        request.judgeId()));
    }
}
