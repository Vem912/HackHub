package it.unicam.cs.ids.hackhub.application.dto.result;

import java.util.UUID;

/** Result of a recorded evaluation. */
public record EvaluateSubmissionResult(
        UUID evaluationId,
        UUID submissionId,
        UUID judgeId,
        int score,
        String judgment,
        long evaluatedSubmissionsCount
) {
}
