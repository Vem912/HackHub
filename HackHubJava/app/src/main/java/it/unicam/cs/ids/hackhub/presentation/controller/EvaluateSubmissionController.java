package it.unicam.cs.ids.hackhub.presentation.controller;

import it.unicam.cs.ids.hackhub.application.dto.command.EvaluateSubmissionCommand;
import it.unicam.cs.ids.hackhub.application.port.in.EvaluateSubmissionUseCase;
import it.unicam.cs.ids.hackhub.presentation.dto.request.EvaluateSubmissionRequest;
import it.unicam.cs.ids.hackhub.presentation.dto.response.EvaluationResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/submissions/{submissionId}/evaluation")
public class EvaluateSubmissionController {
    private final EvaluateSubmissionUseCase evaluateUseCase;

    public EvaluateSubmissionController(EvaluateSubmissionUseCase evaluateUseCase) {
        this.evaluateUseCase = Objects.requireNonNull(evaluateUseCase);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EvaluationResponse evaluate(
            @PathVariable String submissionId,
            @Valid @RequestBody EvaluateSubmissionRequest request) {
        var result = evaluateUseCase.evaluate(new EvaluateSubmissionCommand(
                UUID.fromString(submissionId),
                UUID.fromString(request.judgeId()),
                request.score(),
                request.judgment()));
        return new EvaluationResponse(
                result.evaluationId().toString(),
                result.submissionId().toString(),
                result.judgeId().toString(),
                result.score(),
                result.judgment());
    }
}
