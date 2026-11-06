package it.unicam.cs.ids.hackhub.presentation.dto.response;

public record EvaluateSubmissionResponse(
        String evaluationId,
        String submissionId,
        String judgeId,
        int score,
        String judgment,
        long evaluatedSubmissionsCount
) {
}
