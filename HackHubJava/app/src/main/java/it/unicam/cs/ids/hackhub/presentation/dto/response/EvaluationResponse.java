package it.unicam.cs.ids.hackhub.presentation.dto.response;

/** Common HTTP response for a submission evaluation. */
public record EvaluationResponse(
        String evaluationId,
        String submissionId,
        String judgeId,
        double score,
        String judgment
) {
}
