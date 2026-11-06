package it.unicam.cs.ids.hackhub.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EvaluateSubmissionRequest(
        @NotBlank String submissionId,
        @NotNull @Min(0) @Max(10) Integer score,
        @NotBlank String judgment,
        @NotBlank String judgeId
) {
}
