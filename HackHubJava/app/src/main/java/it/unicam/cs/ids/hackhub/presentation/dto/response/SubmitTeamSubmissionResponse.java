package it.unicam.cs.ids.hackhub.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

/** HTTP response returned after a submission is created or replaced. */
public record SubmitTeamSubmissionResponse(
        UUID submissionId,
        UUID teamId,
        UUID hackathonId,
        String fileName,
        LocalDateTime submittedAt,
        boolean updated
) {
}
