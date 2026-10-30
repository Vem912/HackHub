package it.unicam.cs.ids.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

/** HTTP payload for creating a team; the creator identifier is represented as a string. */
public record CreateTeamRequest(
        @NotBlank String name,
        @NotBlank String creatorId,
        @NotBlank String paymentDetails
) {
}
