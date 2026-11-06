package it.unicam.cs.ids.hackhub.presentation.controller;

import it.unicam.cs.ids.hackhub.application.dto.command.EvaluateSubmissionCommand;
import it.unicam.cs.ids.hackhub.application.port.in.EvaluateSubmissionUseCase;
import it.unicam.cs.ids.hackhub.presentation.dto.request.EvaluateSubmissionRequest;
import it.unicam.cs.ids.hackhub.presentation.dto.response.EvaluateSubmissionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/submissions/evaluations")
public class EvaluationController {
    private final EvaluateSubmissionUseCase evaluateUseCase;

    public EvaluationController(EvaluateSubmissionUseCase evaluateUseCase) {
        this.evaluateUseCase = Objects.requireNonNull(evaluateUseCase);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EvaluateSubmissionResponse evaluate(
            @Valid @RequestBody EvaluateSubmissionRequest request) {
        var result = evaluateUseCase.evaluate(new EvaluateSubmissionCommand(
                UUID.fromString(request.submissionId()),
                UUID.fromString(request.judgeId()),
                request.score(),
                request.judgment()));
        return new EvaluateSubmissionResponse(
                result.evaluationId().toString(),
                result.submissionId().toString(),
                result.judgeId().toString(),
                result.score(),
                result.judgment(),
                result.evaluatedSubmissionsCount());
    }
}
