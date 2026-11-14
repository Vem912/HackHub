package it.unicam.cs.ids.hackhub.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record EvaluateSubmissionRequest(
        @NotNull @Min(0) @Max(10) BigDecimal score,
        @NotBlank String judgment,
        @NotNull UUID judgeId
) {
}
