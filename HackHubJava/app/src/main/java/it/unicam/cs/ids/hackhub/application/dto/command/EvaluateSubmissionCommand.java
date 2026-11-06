package it.unicam.cs.ids.hackhub.application.dto.command;

import java.util.UUID;

/** Input for recording a judge's evaluation. */
public record EvaluateSubmissionCommand(
        UUID submissionId,
        UUID judgeId,
        int score,
        String judgment
) {
}
