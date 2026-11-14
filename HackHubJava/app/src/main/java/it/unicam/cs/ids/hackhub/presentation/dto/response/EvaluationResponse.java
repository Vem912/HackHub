package it.unicam.cs.ids.hackhub.presentation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

/** Common HTTP response for a submission evaluation. */
public record EvaluationResponse(
        UUID evaluationId,
        UUID submissionId,
        UUID judgeId,
        BigDecimal score,
        String judgment
) {
}
