package it.unicam.cs.ids.hackhub.application.dto.result;

import java.time.LocalDateTime;
import java.util.UUID;

/** Result of a successful first submission or replacement. */
public record SubmitTeamSubmissionResult(
        UUID submissionId,
        UUID teamId,
        UUID hackathonId,
        String fileName,
        LocalDateTime submittedAt,
        boolean updated
) {
}
