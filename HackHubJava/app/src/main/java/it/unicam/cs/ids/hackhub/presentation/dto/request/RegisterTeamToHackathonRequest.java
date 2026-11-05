package it.unicam.cs.ids.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

/** HTTP payload for registering a team in a hackathon. */
public record RegisterTeamToHackathonRequest(
        @NotBlank String userId,
        @NotBlank String teamId,
        @NotBlank String hackathonId
) {
}
