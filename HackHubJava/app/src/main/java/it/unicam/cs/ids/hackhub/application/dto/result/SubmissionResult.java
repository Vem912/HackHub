package it.unicam.cs.ids.hackhub.application.dto.result;

import java.time.LocalDateTime;
import java.util.UUID;

/** Submission data exposed to the judging workflow. */
public record SubmissionResult(
        UUID submissionId,
        UUID teamId,
        String title,
        String description,
        String fileName,
        byte[] fileContent,
        LocalDateTime submittedAt
) {
    public SubmissionResult {
        fileContent = java.util.Arrays.copyOf(fileContent, fileContent.length);
    }
}
