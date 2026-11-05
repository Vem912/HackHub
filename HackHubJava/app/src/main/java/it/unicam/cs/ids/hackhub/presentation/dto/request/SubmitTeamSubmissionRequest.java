package it.unicam.cs.ids.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

/** Multipart HTTP payload for submitting or replacing a team's submission. */
public record SubmitTeamSubmissionRequest(
        @NotBlank String userId,
        @NotBlank String title,
        @NotBlank String description,
        @NotNull MultipartFile file
) {
}
