package it.unicam.cs.ids.hackhub.application.dto.result;

import java.math.BigDecimal;
import java.util.UUID;

/** Common application result for a submission evaluation. */
public record EvaluationResult(
        UUID evaluationId,
        UUID submissionId,
        UUID judgeId,
        BigDecimal score,
        String judgment
) {
}
