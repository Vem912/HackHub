package it.unicam.cs.ids.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

/** HTTP payload for completing a hackathon evaluation phase. */
public record CompleteHackathonEvaluationRequest(
        @NotBlank String judgeId
) {
}
