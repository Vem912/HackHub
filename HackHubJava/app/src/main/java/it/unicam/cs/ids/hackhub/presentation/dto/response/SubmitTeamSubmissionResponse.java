package it.unicam.cs.ids.hackhub.presentation.dto.response;

import java.time.LocalDateTime;

/** HTTP response returned after a submission is created or replaced. */
public record SubmitTeamSubmissionResponse(
        String submissionId,
        String teamId,
        String hackathonId,
        String fileName,
        LocalDateTime submittedAt,
        boolean updated
) {
}
