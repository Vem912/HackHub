package it.unicam.cs.ids.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/** Multipart HTTP payload for submitting or replacing a team's submission. */
public record SubmitTeamSubmissionRequest(
        @NotNull UUID userId,
        @NotBlank String title,
        @NotBlank String description,
        @NotNull MultipartFile file
) {
//    @AssertTrue(message = "file must be present, non-empty, and have a filename")
//    public boolean hasValidFile() {
//        return file != null
//                && !file.isEmpty()
//                && file.getOriginalFilename() != null
//                && !file.getOriginalFilename().isBlank();
//    }
}
