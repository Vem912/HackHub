package it.unicam.cs.ids.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/** HTTP payload for registering a team in a hackathon. */
public record RegisterTeamToHackathonRequest(
        @NotNull UUID userId,
        @NotNull UUID teamId
) {
}
