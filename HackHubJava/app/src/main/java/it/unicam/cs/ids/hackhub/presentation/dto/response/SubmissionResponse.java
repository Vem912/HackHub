package it.unicam.cs.ids.hackhub.presentation.dto.response;

import java.time.LocalDateTime;

public record SubmissionResponse(
        String submissionId,
        String teamId,
        String title,
        String description,
        String fileName,
        byte[] fileContent,
        LocalDateTime submittedAt
) {
}
