package it.unicam.cs.ids.hackhub.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubmissionResponse(
        UUID submissionId,
        UUID teamId,
        String title,
        String description,
        String fileName,
        LocalDateTime submittedAt
) {
}
